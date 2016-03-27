package ru.mw.util.xml.tests;

import org.junit.Test;
import ru.mw.util.xml.XMLBuilder;

import static org.junit.Assert.assertEquals;

public class CreateXmlTest {

    @Test
    public void should_create_simple_xml() {
        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<test a=\"b\"/>";
        XMLBuilder builder = new XMLBuilder();
        builder.tag("test").attr("a", "b").close();
        assertEquals("Strings should be equal", xml, builder.toString());
    }

    @Test
    public void should_create_complex_xml() {
        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<request v=\"1\">\n" +
                " <request-type>pay</request-type>\n" +
                " <extra name=\"token\">2e23e2332</extra>\n" +
                " <check>\n" +
                "  <payment>\n" +
                "   <to>\n" +
                "    <xxx>111</xxx>\n" +
                "    <account-number>222</account-number>\n" +
                "   </to>\n" +
                "  </payment>\n" +
                " </check>\n" +
                "</request>";
        XMLBuilder builder = new XMLBuilder();
        builder.tag("request").attr("v", 1);
        builder.tag("request-type", "pay");
        builder.tag("extra").attr("name", "token").text("2e23e2332").up();
        builder.tag("check");
        builder.tag("payment");
        builder.tag("to");
        builder.tag("xxx", 111);
        builder.tag("account-number", 222);
        builder.close();
        assertEquals("Strings should be equal", xml, builder.toString());
    }

    @Test
    public void long_attr_and_short_attr_should_be_equal() {
        XMLBuilder first = new XMLBuilder();
        first.tag("vvv").attr("sss", 11).text("121");
        first.close();
        XMLBuilder second = new XMLBuilder();
        second.tag("vvv").a("sss", 11).t("121");
        second.close();
        assertEquals("Strings should be equal", first.toString(), second.toString());
    }

    @Test
    public void xml_declaration_should_be_added_automatically() {
        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
        XMLBuilder builder = new XMLBuilder();
        assertEquals("Strings should be equal", xml, builder.toString());
    }

}
