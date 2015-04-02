package gov.hhs.onc.dcdt.crypto.utils;

import gov.hhs.onc.dcdt.crypto.certs.CertificateAltNameType;
import gov.hhs.onc.dcdt.crypto.certs.CertificateException;
import gov.hhs.onc.dcdt.crypto.certs.impl.CertificateSerialNumberImpl;
import gov.hhs.onc.dcdt.discovery.BindingType;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolCollectionUtils;
import gov.hhs.onc.dcdt.utils.ToolStreamUtils;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;

public abstract class CertificateNameUtils {
    public static Pair<CertificateAltNameType, GeneralName> transformCertificateAltNameListEntry(List<?> altNameList) {
        CertificateAltNameType altNameType = CryptographyUtils.findByTag(CertificateAltNameType.class, ((Integer) altNameList.get(0)));

        return ((altNameType != null) ? new MutablePair<>(altNameType, new GeneralName(altNameType.getTag(), ((String) altNameList.get(1)))) : null);
    }

    public static Pair<CertificateAltNameType, GeneralName> transformCertificateAltNameEntry(GeneralName altName) {
        CertificateAltNameType altNameType = CryptographyUtils.findByTag(CertificateAltNameType.class, altName.getTagNo());

        return ((altNameType != null) ? new MutablePair<>(altNameType, altName) : null);
    }

    @Nullable
    public static GeneralNames setMailAddress(@Nullable GeneralNames altNames, @Nullable MailAddress mailAddr) {
        BindingType mailAddrBindingType;

        if ((mailAddr == null) || !(mailAddrBindingType = mailAddr.getBindingType()).isBound()) {
            return altNames;
        }

        Map<CertificateAltNameType, GeneralName> altNameMap = mapAltNames(altNames);

        if (altNameMap == null) {
            altNameMap = new LinkedHashMap<>();
        }

        if (mailAddrBindingType.isAddressBound()) {
            altNameMap.put(CertificateAltNameType.RFC822_NAME,
                new GeneralName(CertificateAltNameType.RFC822_NAME.getTag(), mailAddr.toAddress(mailAddrBindingType)));
        } else {
            altNameMap.put(CertificateAltNameType.DNS_NAME, new GeneralName(CertificateAltNameType.DNS_NAME.getTag(), mailAddr.toAddress(mailAddrBindingType)));
        }

        return buildAltNames(altNameMap);
    }

    @Nullable
    public static GeneralNames buildIssuerAltNames(X509Certificate cert) throws CertificateException {
        return buildAltNames(mapIssuerAltNames(cert));
    }

    @Nullable
    public static Map<CertificateAltNameType, GeneralName> mapIssuerAltNames(X509Certificate cert) throws CertificateException {
        try {
            return mapAltNames(cert.getIssuerAlternativeNames());
        } catch (CertificateParsingException e) {
            throw new CertificateException(String.format("Unable to map X509 certificate (subj={%s}, issuer={%s}, serialNum=%s) issuer alternative name(s).",
                cert.getSubjectX500Principal().getName(), cert.getIssuerX500Principal(), new CertificateSerialNumberImpl(cert.getSerialNumber())), e);
        }
    }

    @Nullable
    public static GeneralNames buildSubjectAltNames(X509Certificate cert) throws CertificateException {
        return buildAltNames(mapSubjectAltNames(cert));
    }

    @Nullable
    public static Map<CertificateAltNameType, GeneralName> mapSubjectAltNames(X509Certificate cert) throws CertificateException {
        try {
            return mapAltNames(cert.getSubjectAlternativeNames());
        } catch (CertificateParsingException e) {
            throw new CertificateException(String.format("Unable to map X509 certificate (subj={%s}, issuer={%s}, serialNum=%s) subject alternative name(s).",
                cert.getSubjectX500Principal().getName(), cert.getIssuerX500Principal(), new CertificateSerialNumberImpl(cert.getSerialNumber())), e);
        }
    }

    @Nullable
    public static Map<CertificateAltNameType, GeneralName> mapAltNames(@Nullable Collection<List<?>> altNames) {
        return altNames != null ? ToolStreamUtils.stream(altNames).map(CertificateNameUtils::transformCertificateAltNameListEntry).filter(Objects::nonNull)
            .collect(Collectors.toMap(Pair::getLeft, Pair::getRight, (a, b) -> b, LinkedHashMap::new)) : null;
    }

    @Nullable
    public static Map<CertificateAltNameType, GeneralName> mapAltNames(@Nullable GeneralNames altNames) {
        return altNames != null ? ToolStreamUtils.stream(ToolArrayUtils.asList(altNames.getNames())).map(
            CertificateNameUtils::transformCertificateAltNameEntry).filter(Objects::nonNull).collect(Collectors.toMap(Pair::getLeft, Pair::getRight, (a, b)
            -> b, LinkedHashMap::new)) : null;
    }

    @Nullable
    public static GeneralNames buildAltNames(@Nullable Map<CertificateAltNameType, GeneralName> altNameMap) {
        return ((altNameMap != null) ? new GeneralNames(ToolCollectionUtils.toArray(altNameMap.values(), GeneralName.class)) : null);
    }
}
