package gov.hhs.onc.dcdt.crypto.utils;

import gov.hhs.onc.dcdt.crypto.certs.CertificateAltNameType;
import gov.hhs.onc.dcdt.crypto.certs.CertificateException;
import gov.hhs.onc.dcdt.crypto.certs.impl.CertificateSerialNumberImpl;
import gov.hhs.onc.dcdt.discovery.BindingType;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.utils.ToolEnumUtils;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public abstract class CertificateNameUtils {
    @Nullable
    public static GeneralNames setMailAddress(@Nullable GeneralNames altNames, @Nullable MailAddress mailAddr) {
        BindingType mailAddrBindingType;

        if ((mailAddr == null) || !(mailAddrBindingType = mailAddr.getBindingType()).isBound()) {
            return altNames;
        }

        MultiValueMap<CertificateAltNameType, GeneralName> altNameMap = mapAltNames(altNames);

        if (altNameMap == null) {
            altNameMap = new LinkedMultiValueMap<>();
        }

        if (mailAddrBindingType.isAddressBound()) {
            altNameMap.set(CertificateAltNameType.RFC822_NAME,
                new GeneralName(CertificateAltNameType.RFC822_NAME.getTag(), mailAddr.toAddress(mailAddrBindingType)));
        } else {
            altNameMap.set(CertificateAltNameType.DNS_NAME, new GeneralName(CertificateAltNameType.DNS_NAME.getTag(), mailAddr.toAddress(mailAddrBindingType)));
        }

        return buildAltNames(altNameMap);
    }

    @Nullable
    public static GeneralNames buildIssuerAltNames(X509Certificate cert) throws CertificateException {
        return buildAltNames(mapIssuerAltNames(cert));
    }

    @Nullable
    public static MultiValueMap<CertificateAltNameType, GeneralName> mapIssuerAltNames(X509Certificate cert) throws CertificateException {
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
    public static MultiValueMap<CertificateAltNameType, GeneralName> mapSubjectAltNames(X509Certificate cert) throws CertificateException {
        try {
            return mapAltNames(cert.getSubjectAlternativeNames());
        } catch (CertificateParsingException e) {
            throw new CertificateException(String.format("Unable to map X509 certificate (subj={%s}, issuer={%s}, serialNum=%s) subject alternative name(s).",
                cert.getSubjectX500Principal().getName(), cert.getIssuerX500Principal(), new CertificateSerialNumberImpl(cert.getSerialNumber())), e);
        }
    }

    @Nullable
    public static MultiValueMap<CertificateAltNameType, GeneralName> mapAltNames(@Nullable Collection<List<?>> altNames) {
        if (altNames == null) {
            return null;
        }

        MultiValueMap<CertificateAltNameType, GeneralName> altNameMap = new LinkedMultiValueMap<>(CertificateAltNameType.values().length);

        altNames.stream().forEach(altNameList -> {
            Integer altNameTag = ((Integer) altNameList.get(0));
            CertificateAltNameType altNameType = ToolEnumUtils.findByPredicate(CertificateAltNameType.class, enumItem -> (enumItem.getTag() == altNameTag));

            if (altNameType != null) {
                altNameMap.add(altNameType, new GeneralName(altNameTag, ((String) altNameList.get(1))));
            }
        });

        return altNameMap;
    }

    @Nullable
    public static MultiValueMap<CertificateAltNameType, GeneralName> mapAltNames(@Nullable GeneralNames altNames) {
        if (altNames == null) {
            return null;
        }

        MultiValueMap<CertificateAltNameType, GeneralName> altNameMap = new LinkedMultiValueMap<>(CertificateAltNameType.values().length);

        Stream.of(altNames.getNames()).forEach(altNameItem -> {
            int altNameTag = altNameItem.getTagNo();
            CertificateAltNameType altNameType = ToolEnumUtils.findByPredicate(CertificateAltNameType.class, enumItem -> (enumItem.getTag() == altNameTag));

            if (altNameType != null) {
                altNameMap.add(altNameType, altNameItem);
            }
        });

        return altNameMap;
    }

    @Nullable
    public static GeneralNames buildAltNames(@Nullable Map<CertificateAltNameType, ? extends Collection<GeneralName>> altNameMap) {
        return ((altNameMap != null)
            ? new GeneralNames(altNameMap.keySet().stream().flatMap(altNameType -> altNameMap.get(altNameType).stream()).toArray(GeneralName[]::new)) : null);
    }
}
