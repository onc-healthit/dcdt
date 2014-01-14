package gov.hhs.onc.dcdt.crypto.utils;


import gov.hhs.onc.dcdt.test.ToolTestNgUnitTests;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.collections4.CollectionUtils;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = { "dcdt.test.all", "dcdt.test.unit.all", "dcdt.test.unit.crypto.all", "dcdt.test.unit.crypto.utils.all", "dcdt.test.unit.crypto.utils.x500" })
public class X500UtilsUnitTests extends ToolTestNgUnitTests {
    private final static ASN1ObjectIdentifier TEST_X500_NAME_MAIL_ADDR_OID = BCStyle.EmailAddress;
    private final static String TEST_X500_NAME_MAIL_ADDR_VALUE_STR = "x500@test.direct-test.com";
    private final static ASN1ObjectIdentifier TEST_X500_NAME_COMMON_NAME_OID = BCStyle.CN;
    private final static String TEST_X500_NAME_COMMON_NAME_VALUE_STR = "x500.test.direct-test.com";

    private Map<ASN1ObjectIdentifier, ASN1Encodable> testX500NameAttrMap = new LinkedHashMap<>(2);

    @Test(dependsOnMethods = { "testToEncodeableValue" })
    public void testBuildName() {
        X500Name testX500Name = X500Utils.buildName(this.testX500NameAttrMap);
        Assert.assertNotNull(testX500Name, "X500 name is null");

        Map<ASN1ObjectIdentifier, ASN1Encodable> testX500NameRdnAttrMap = X500Utils.mapAttributes(testX500Name.getRDNs());
        Assert.assertEquals(CollectionUtils.size(testX500NameRdnAttrMap), 2, "Incorrect X500 attribute map size.");
        Assert.assertEquals(testX500NameRdnAttrMap, this.testX500NameAttrMap, "X500 attribute maps are not equal.");
    }

    @Test
    public void testToEncodeableValue() {
        testX500NameAttrMap.put(TEST_X500_NAME_MAIL_ADDR_OID, X500Utils.toEncodableValue(TEST_X500_NAME_MAIL_ADDR_OID, TEST_X500_NAME_MAIL_ADDR_VALUE_STR));
        testX500NameAttrMap.put(TEST_X500_NAME_COMMON_NAME_OID,
            X500Utils.toEncodableValue(TEST_X500_NAME_COMMON_NAME_OID, TEST_X500_NAME_COMMON_NAME_VALUE_STR));
    }
}
