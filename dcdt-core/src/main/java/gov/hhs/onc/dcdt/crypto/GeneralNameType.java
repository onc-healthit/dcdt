package gov.hhs.onc.dcdt.crypto;

import org.bouncycastle.asn1.x509.GeneralName;

public enum GeneralNameType implements CryptographyTaggedIdentifier {
    OTHER_NAME("otherName", GeneralName.otherName), RFC822_NAME("rfc822Name", GeneralName.rfc822Name), DNS_NAME("dNSName", GeneralName.dNSName), X400_ADDRESS(
        "x400Address", GeneralName.x400Address), DIRECTORY_NAME("directoryName", GeneralName.directoryName), EDI_PARTY_NAME("ediPartyName",
        GeneralName.ediPartyName), UNIFORM_RESOURCE_IDENTIFIER("uniformResourceIdentifier", GeneralName.uniformResourceIdentifier), IP_ADDRESS("iPAddress",
        GeneralName.iPAddress), REGISTERED_ID("registeredID", GeneralName.registeredID);

    private final String id;
    private final int tag;

    private GeneralNameType(String id, int tag) {
        this.id = id;
        this.tag = tag;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public int getTag() {
        return this.tag;
    }
}
