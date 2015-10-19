package gov.hhs.onc.dcdt.crypto.certs.path.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.crypto.certs.path.CertificatePathResolver;

public class CertificatePathResolverImpl extends AbstractToolBean implements CertificatePathResolver {
    
    
    // @formatter:off
    /*
    private static class AccessLocationAccessDescriptionTransformer extends AbstractToolTransformer<AccessDescription, String> {
        public final static AccessLocationAccessDescriptionTransformer INSTANCE = new AccessLocationAccessDescriptionTransformer();

        @Override
        protected String transformInternal(AccessDescription accessDesc) throws Exception {
            return accessDesc.getAccessLocation().getName().toString();
        }
    }

    private static class CaIssuersAccessDescriptionPredicate extends AbstractToolPredicate<AccessDescription> {
        public final static CaIssuersAccessDescriptionPredicate INSTANCE = new CaIssuersAccessDescriptionPredicate();

        @Override
        protected boolean evaluateInternal(AccessDescription accessDesc) throws Exception {
            return accessDesc.getAccessMethod().equals(AccessDescription.id_ad_caIssuers);
        }
    }

    private final static Logger LOGGER = LoggerFactory.getLogger(CertificatePathResolverImpl.class);

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private CertificateDiscoveryService certDiscoveryService;

    private int issuerAccessLocUrlConnectTimeout;
    private int issuerAccessLocUrlReadTimeout;

    @Override
    public List<CertificateInfo> resolvePath(CertificateInfo certInfo) throws CryptographyException {
        return ToolListUtils.addFirst(this.resolveIssuers(certInfo), certInfo);
    }

    @Override
    public List<CertificateInfo> resolveIssuers(CertificateInfo certInfo) throws CryptographyException {
        List<CertificateInfo> issuerCertInfos = new ArrayList<>();
        CertificateInfo issuerCertInfo;

        while (!certInfo.isSelfIssued() && ((issuerCertInfo = this.resolveIssuer(certInfo)) != null)) {
            issuerCertInfos.add(issuerCertInfo);

            certInfo = issuerCertInfo;
        }

        return issuerCertInfos;
    }

    @Nullable
    @Override
    public CertificateInfo resolveIssuer(CertificateInfo certInfo) throws CryptographyException {
        if (certInfo.isSelfIssued()) {
            return certInfo;
        }

        X509Certificate cert = certInfo.getCertificate();
        List<CertificateInfo> issuerCerts = new ArrayList<>();
        CertificateInfo issuerCertInfo;

        if (certInfo.hasExtension(Extension.authorityInfoAccess)) {
            URLConnection issuerAccessLocUrlConn;

            for (String issuerAccessLoc : CollectionUtils.collect(CollectionUtils.select(
                ToolArrayUtils.asList(AuthorityInformationAccess.getInstance(certInfo.getExtension(Extension.authorityInfoAccess)).getAccessDescriptions()),
                CaIssuersAccessDescriptionPredicate.INSTANCE), AccessLocationAccessDescriptionTransformer.INSTANCE)) {
                try {
                    issuerAccessLocUrlConn = new URL(issuerAccessLoc).openConnection();
                    issuerAccessLocUrlConn.setConnectTimeout(this.issuerAccessLocUrlConnectTimeout);
                    issuerAccessLocUrlConn.setReadTimeout(this.issuerAccessLocUrlReadTimeout);

                    try (InputStream issuerAccessLocUrlInStream = issuerAccessLocUrlConn.getInputStream()) {
                        issuerCerts.add((issuerCertInfo =
                            new CertificateInfoImpl(CertificateUtils.readCertificate(issuerAccessLocUrlInStream, CertificateType.X509, DataEncoding.DER))));

                        // noinspection ConstantConditions
                        LOGGER
                            .info(String
                                .format(
                                    "Resolved certificate (subj={%s}, serialNum=%s) issuer (subj={%s}, serialNum=%s) using Authority Information Access X509v3 extension issuer location URL: %s",
                                    cert.getSubjectX500Principal().getName(), certInfo.getSerialNumber(), cert.getIssuerX500Principal().getName(),
                                    issuerCertInfo.getSerialNumber(), issuerAccessLoc));
                    }
                } catch (MalformedURLException e) {
                    // noinspection ConstantConditions
                    throw new CertificateException(
                        String.format(
                            "Certificate (subj={%s}, serialNum=%s) Authority Information Access X509v3 extension issuer (subj={%s}) location URL is a malformed: %s",
                            cert.getSubjectX500Principal().getName(), certInfo.getSerialNumber(), cert.getIssuerX500Principal().getName(), issuerAccessLoc), e);
                } catch (CryptographyException | IOException e) {
                    // noinspection ConstantConditions
                    throw new CertificateException(
                        String.format(
                            "Unable to read certificate (subj={%s}, serialNum=%s) Authority Information Access X509v3 extension issuer (subj={%s}) location URL: %s",
                            cert.getSubjectX500Principal().getName(), certInfo.getSerialNumber(), cert.getIssuerX500Principal().getName(), issuerAccessLoc), e);
                }
            }
        }

        CertificateName issuerName;

        // noinspection ConstantConditions
        if (issuerCerts.isEmpty() && (issuerName = certInfo.getIssuerName()).hasMailAddress()) {
            MailAddress issuerMailAddr = issuerName.getMailAddress();
            CertificateDiscoveryStep issuerDiscoveryLastStep = ToolListUtils.getLast(this.certDiscoveryService.discoverCertificates(issuerMailAddr));
            CertificateValidationStep issuerDiscoveryCertValidationStep;

            // noinspection ConstantConditions
            if (issuerDiscoveryLastStep.isSuccess() && ToolClassUtils.isAssignable(issuerDiscoveryLastStep.getClass(), CertificateValidationStep.class)
                && (issuerDiscoveryCertValidationStep = ((CertificateValidationStep) issuerDiscoveryLastStep)).hasValidCertificateInfo()
                && (issuerCertInfo = issuerDiscoveryCertValidationStep.getValidCertificateInfo()).hasCertificate()) {
                issuerCerts.add(issuerCertInfo);

                // noinspection ConstantConditions
                LOGGER.info(String.format("Discovered certificate (subj={%s}, serialNum=%s) issuer (subj={%s}, serialNum=%s) using issuer mail address: %s",
                    cert.getSubjectX500Principal().getName(), certInfo.getSerialNumber(), cert.getIssuerX500Principal().getName(),
                    issuerCertInfo.getSerialNumber(), issuerMailAddr));
            }
        }

        return ToolListUtils.getFirst(issuerCerts);
    }

    @Override
    public int getIssuerAccessLocationUrlConnectTimeout() {
        return this.issuerAccessLocUrlConnectTimeout;
    }

    @Override
    public void setIssuerAccessLocationUrlConnectTimeout(@Nonnegative int issuerAccessLocUrlConnectTimeout) {
        this.issuerAccessLocUrlConnectTimeout = issuerAccessLocUrlConnectTimeout;
    }

    @Override
    public int getIssuerAccessLocationUrlReadTimeout() {
        return this.issuerAccessLocUrlReadTimeout;
    }

    @Override
    public void setIssuerAccessLocationUrlReadTimeout(@Nonnegative int issuerAccessLocUrlReadTimeout) {
        this.issuerAccessLocUrlReadTimeout = issuerAccessLocUrlReadTimeout;
    }
    */
    // @formatter:on
}
