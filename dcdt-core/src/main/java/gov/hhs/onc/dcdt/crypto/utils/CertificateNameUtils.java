package gov.hhs.onc.dcdt.crypto.utils;

import gov.hhs.onc.dcdt.collections.impl.AbstractToolTransformer;
import gov.hhs.onc.dcdt.crypto.certs.CertificateAltNameType;
import gov.hhs.onc.dcdt.crypto.certs.CertificateException;
import gov.hhs.onc.dcdt.crypto.certs.impl.CertificateSerialNumberImpl;
import gov.hhs.onc.dcdt.discovery.BindingType;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolCollectionUtils;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.PredicateUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;

public abstract class CertificateNameUtils {
    public static class CertificateAltNameListEntryTransformer extends AbstractToolTransformer<List<?>, Pair<CertificateAltNameType, GeneralName>> {
        public final static CertificateAltNameListEntryTransformer INSTANCE = new CertificateAltNameListEntryTransformer();

        @Nullable
        @Override
        protected Pair<CertificateAltNameType, GeneralName> transformInternal(List<?> altNameList) throws Exception {
            CertificateAltNameType altNameType = CryptographyUtils.findByTag(CertificateAltNameType.class, ((Integer) altNameList.get(0)));

            return ((altNameType != null) ? new MutablePair<>(altNameType, new GeneralName(altNameType.getTag(), ((String) altNameList.get(1)))) : null);
        }
    }

    public static class CertificateAltNameEntryTransformer extends AbstractToolTransformer<GeneralName, Pair<CertificateAltNameType, GeneralName>> {
        public final static CertificateAltNameEntryTransformer INSTANCE = new CertificateAltNameEntryTransformer();

        @Nullable
        @Override
        protected Pair<CertificateAltNameType, GeneralName> transformInternal(GeneralName altName) throws Exception {
            CertificateAltNameType altNameType = CryptographyUtils.findByTag(CertificateAltNameType.class, altName.getTagNo());

            return ((altNameType != null) ? new MutablePair<>(altNameType, altName) : null);
        }
    }

    @Nullable
    public static GeneralNames setMailAddress(@Nullable GeneralNames altNames, @Nullable MailAddress mailAddr) {
        BindingType mailAddrBindingType;

        if ((mailAddr == null) || !(mailAddrBindingType = mailAddr.getBindingType()).isBound()) {
            return altNames;
        }

        Map<CertificateAltNameType, Set<GeneralName>> altNameMap = mapAltNames(altNames);
        Set<GeneralName> generalNames = new HashSet<>();

        if (altNameMap == null) {
            altNameMap = new LinkedHashMap<>();
        }

        if (mailAddrBindingType.isAddressBound()) {
            generalNames.add(new GeneralName(CertificateAltNameType.RFC822_NAME.getTag(), mailAddr.toAddress(mailAddrBindingType)));
            altNameMap.put(CertificateAltNameType.RFC822_NAME, generalNames);
        } else {
            generalNames.add(new GeneralName(CertificateAltNameType.DNS_NAME.getTag(), mailAddr.toAddress(mailAddrBindingType)));
            altNameMap.put(CertificateAltNameType.DNS_NAME, generalNames);
        }

        return buildAltNames(altNameMap);
    }

    @Nullable
    public static GeneralNames buildIssuerAltNames(X509Certificate cert) throws CertificateException {
        return buildAltNames(mapIssuerAltNames(cert));
    }

    @Nullable
    public static Map<CertificateAltNameType, Set<GeneralName>> mapIssuerAltNames(X509Certificate cert) throws CertificateException {
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
    public static Map<CertificateAltNameType, Set<GeneralName>> mapSubjectAltNames(X509Certificate cert) throws CertificateException {
        try {
            return mapAltNames(cert.getSubjectAlternativeNames());
        } catch (CertificateParsingException e) {
            throw new CertificateException(String.format("Unable to map X509 certificate (subj={%s}, issuer={%s}, serialNum=%s) subject alternative name(s).",
                cert.getSubjectX500Principal().getName(), cert.getIssuerX500Principal(), new CertificateSerialNumberImpl(cert.getSerialNumber())), e);
        }
    }

    @Nullable
    public static Map<CertificateAltNameType, Set<GeneralName>> mapAltNames(@Nullable Collection<List<?>> altNames) {
        return (altNames != null) ? mapAltNamePairs(
            CollectionUtils.select(CollectionUtils.collect(altNames, CertificateAltNameListEntryTransformer.INSTANCE), PredicateUtils.notNullPredicate()))
            : null;
    }

    @Nullable
    public static Map<CertificateAltNameType, Set<GeneralName>> mapAltNames(@Nullable GeneralNames altNames) {
        return ((altNames != null) ? mapAltNamePairs(
            CollectionUtils.select(CollectionUtils.collect(ToolArrayUtils.asList(altNames.getNames()), CertificateAltNameEntryTransformer.INSTANCE),
                PredicateUtils.notNullPredicate())) : null);
    }

    public static Map<CertificateAltNameType, Set<GeneralName>> mapAltNamePairs(Collection<Pair<CertificateAltNameType, GeneralName>> pairs) {
        Map<CertificateAltNameType, Set<GeneralName>> altNameMap = new LinkedHashMap<>(CertificateAltNameType.values().length);

        for (Pair<CertificateAltNameType, GeneralName> pair : pairs) {
            CertificateAltNameType certAltNameType = pair.getLeft();

            if (!altNameMap.containsKey(certAltNameType)) {
                altNameMap.put(certAltNameType, new HashSet<GeneralName>());
            }

            altNameMap.get(certAltNameType).add(pair.getRight());
        }

        return altNameMap;
    }

    @Nullable
    public static GeneralNames buildAltNames(@Nullable Map<CertificateAltNameType, Set<GeneralName>> altNameMap) {
        return ((altNameMap != null) ? new GeneralNames(ToolCollectionUtils.toArray(
            ToolCollectionUtils.addAll(new ArrayList<GeneralName>(), altNameMap.values()), GeneralName.class)) : null);
    }
}
