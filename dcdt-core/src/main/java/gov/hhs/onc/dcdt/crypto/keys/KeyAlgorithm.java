package gov.hhs.onc.dcdt.crypto.keys;

import gov.hhs.onc.dcdt.crypto.CryptographyAlgorithm;
import gov.hhs.onc.dcdt.dns.DnsKeyAlgorithm;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public enum KeyAlgorithm implements CryptographyAlgorithm {
    RSA("RSA", PKCSObjectIdentifiers.rsaEncryption, "X.509", "PKCS#8", 512, DnsKeyAlgorithm.RSASHA1);

    private final String name;
    private final ASN1ObjectIdentifier oid;
    private final AlgorithmIdentifier id;
    private final String publicFormat;
    private final String privateFormat;
    private final int keySizeMin;
    private final DnsKeyAlgorithm dnsAlg;

    private KeyAlgorithm(String name, ASN1ObjectIdentifier oid, String publicFormat, String privateFormat, int keySizeMin, DnsKeyAlgorithm dnsAlg) {
        this.name = name;
        this.oid = oid;
        this.id = new AlgorithmIdentifier(this.oid);
        this.publicFormat = publicFormat;
        this.privateFormat = privateFormat;
        this.keySizeMin = keySizeMin;
        this.dnsAlg = dnsAlg;
    }

    public DnsKeyAlgorithm getDnsAlgorithm() {
        return this.dnsAlg;
    }

    public String getFormat(KeyType keyType) {
        return (keyType == KeyType.PUBLIC) ? this.getPublicFormat() : this.getPrivateFormat();
    }

    @Override
    public AlgorithmIdentifier getId() {
        return this.id;
    }

    public int getKeySizeMin() {
        return this.keySizeMin;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public ASN1ObjectIdentifier getOid() {
        return this.oid;
    }

    public String getPublicFormat() {
        return this.publicFormat;
    }

    public String getPrivateFormat() {
        return this.privateFormat;
    }
}
