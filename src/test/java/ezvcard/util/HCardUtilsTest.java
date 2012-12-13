package ezvcard.util;

import static ezvcard.util.HCardUnitTestUtils.toHtmlElement;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

/*
 Copyright (c) 2012, Michael Angstadt
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met: 

 1. Redistributions of source code must retain the above copyright notice, this
 list of conditions and the following disclaimer. 
 2. Redistributions in binary form must reproduce the above copyright notice,
 this list of conditions and the following disclaimer in the documentation
 and/or other materials provided with the distribution. 

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 The views and conclusions contained in the software and documentation are those
 of the authors and should not be interpreted as representing official policies, 
 either expressed or implied, of the FreeBSD Project.
 */

/**
 * @author Michael Angstadt
 */
public class HCardUtilsTest {
	private static final String newline = System.getProperty("line.separator");

	@Test
	public void getElementValue_text_content() {
		Element element = toHtmlElement("<div>The text</div>");
		assertEquals("The text", HCardUtils.getElementValue(element));
	}

	@Test
	public void getElementValue_line_breaks() {
		Element element = toHtmlElement("<div>The<br>text</div>");
		assertEquals("The" + newline + "text", HCardUtils.getElementValue(element));

		//"value" element
		element = toHtmlElement("<div>The <span class=\"value\">real<br>text</span></div>");
		assertEquals("real" + newline + "text", HCardUtils.getElementValue(element));

		//nested "value" element
		element = toHtmlElement("<div>The <span class=\"value\">real<br>text <span class=\"value\">goes<br>here</span></span></div>");
		assertEquals("real" + newline + "text goes" + newline + "here", HCardUtils.getElementValue(element));
	}

	@Test
	public void getElementValue_ignore_child_tags() {
		Element element = toHtmlElement("<div>The<b>text</b></div>");
		assertEquals("Thetext", HCardUtils.getElementValue(element));
	}

	@Test
	public void getElementValue_ignore_type_text() {
		Element element = toHtmlElement("<div><span class=\"type\">Work</span> is boring.</div>");
		assertEquals("is boring.", HCardUtils.getElementValue(element));

		element = toHtmlElement("<div><b>All <span class=\"type\">work</span> is boring.</b></div>");
		assertEquals("All  is boring.", HCardUtils.getElementValue(element));
	}

	@Test
	public void getElementValue_value_tag() {
		Element element = toHtmlElement("<div>This is <span class=\"value\">the text</span>.</div>");
		assertEquals("the text", HCardUtils.getElementValue(element));
	}

	@Test
	public void getElementValue_multiple_value_tags() {
		Element element = toHtmlElement("<div><span class=\"value\">+1</span>.<span class=\"value\">415</span>.<span class=\"value\">555</span>.<span class=\"value\">1212</span></div>");
		assertEquals("+14155551212", HCardUtils.getElementValue(element));
	}

	@Test
	public void getElementValue_multiple_value_tags_not_direct_child() {
		//@formatter:off
		String html = "";
		html += "<div>";
			html += "<div>Some text</div>";
			html += "<div>";
				html += "<span class=\"value\">This is</span>";
				html += "<div>";
					html += "<div class=\"value\">the value</div>";
				html += "</div>";
				html += "<div class=\"value\">of the element.</div>";
			html += "</div>";
		html += "</div>";
		
		Element element = toHtmlElement(html);
		assertEquals("This isthe valueof the element.", HCardUtils.getElementValue(element));
	}
	
	@Test
	public void getElementValue_nested_value_tags() {
		//@formatter:off
		String html = "";
		html += "<div>";
			html += "<div class=\"value\">the value<span class=\"value\">nested</span></div>";
		html += "</div>";
		//@formatter:on

		Element element = toHtmlElement(html);
		assertEquals("the valuenested", HCardUtils.getElementValue(element));
	}

	@Test
	public void getElementValue_abbr_value() {
		Element element = toHtmlElement("<div>This is <abbr class=\"value\" title=\"1234\">the text</abbr>.</div>");
		assertEquals("1234", HCardUtils.getElementValue(element));
	}

	@Test
	public void getElementValue_abbr_tag_with_title() {
		Element element = toHtmlElement("<abbr class=\"latitude\" title=\"48.816667\">N 48� 81.6667</abbr>");
		assertEquals("48.816667", HCardUtils.getElementValue(element));
	}

	@Test
	public void getElementValue_abbr_tag_without_title() {
		Element element = toHtmlElement("<abbr class=\"latitude\">N 48� 81.6667</abbr>");
		assertEquals("N 48� 81.6667", HCardUtils.getElementValue(element));
	}

	@Test
	public void getElementValue_skip_del_tags() {
		Element element = toHtmlElement("<div>This element contains <del>a lot of</del> text</div>");
		assertEquals("This element contains  text", HCardUtils.getElementValue(element));
	}

	@Test
	public void getElementValue_skip_del_tags_in_value() {
		Element element = toHtmlElement("<div>This element <span class=\"value\">contains <del>a lot of</del> text</span></div>");
		assertEquals("contains  text", HCardUtils.getElementValue(element));
	}

	@Test
	public void getElementValuesByCssClass() {
		//@formatter:off
		StringBuilder html = new StringBuilder();
		html.append("<div class=\"n\">");
			html.append("<div>");
				html.append("<span class=\"family-name\"><b>Doe</b></span>");
			html.append("</div>");
			html.append("<div>");
				html.append("<div>");
					html.append("<span class=\"additional-name\">Smith</span>");
				html.append("</div>");
			html.append("</div>");
			html.append("<span class=\"additional-name\">(<span class=\"value\">Barney</span>)</span>");
		html.append("</div>");
		//@formatter:on

		Element element = toHtmlElement(html.toString());

		List<String> actual = HCardUtils.getElementValuesByCssClass(element, "family-name");
		List<String> expected = Arrays.asList("Doe");
		assertEquals(expected, actual);

		actual = HCardUtils.getElementValuesByCssClass(element, "additional-name");
		expected = Arrays.asList("Smith", "Barney");
		assertEquals(expected, actual);

		actual = HCardUtils.getElementValuesByCssClass(element, "non-existant-class");
		assertTrue(actual.isEmpty());
	}

	@Test
	public void getTypes_none() {
		StringBuilder html = new StringBuilder();
		html.append("<div class=\"adr\">");
		html.append("</div>");

		Element element = toHtmlElement(html.toString());
		List<String> actual = HCardUtils.getTypes(element);
		assertTrue(actual.isEmpty());
	}

	@Test
	public void getTypes_multiple() {
		//@formatter:off
		StringBuilder html = new StringBuilder();
		html.append("<div class=\"adr\">");
			html.append("<span class=\"type\">work</span>");
			html.append("<span class=\"type\">pref</span>");
		html.append("</div>");
		//@formatter:on

		Element element = toHtmlElement(html.toString());
		List<String> actual = HCardUtils.getTypes(element);
		List<String> expected = Arrays.asList("work", "pref");
		assertEquals(expected, actual);
	}

	@Test
	public void getTypes_convert_to_lower_case() {
		//@formatter:off
		StringBuilder html = new StringBuilder();
		html.append("<div class=\"adr\">");
			html.append("<span class=\"type\">wOrk</span>");
			html.append("<span class=\"type\">prEf</span>");
		html.append("</div>");
		//@formatter:on

		Element element = toHtmlElement(html.toString());
		List<String> actual = HCardUtils.getTypes(element);
		List<String> expected = Arrays.asList("work", "pref");
		assertEquals(expected, actual);
	}

	@Test
	public void getTypes_not_direct_descendant() {
		//@formatter:off
		StringBuilder html = new StringBuilder();
		html.append("<div class=\"adr\">");
			html.append("<span class=\"type\">work</span>");
			html.append("<div>");
				html.append("<span class=\"type\">pref</span>");
			html.append("<div>");
		html.append("</div>");
		//@formatter:on

		Element element = toHtmlElement(html.toString());
		List<String> actual = HCardUtils.getTypes(element);
		List<String> expected = Arrays.asList("work", "pref");
		assertEquals(expected, actual);
	}

	@Test
	public void getAbsUrl() {
		Element element = toHtmlElement("<a href=\"data:foo\" />", "http://example.com");
		assertEquals("data:foo", HCardUtils.getAbsUrl(element, "href"));

		element = toHtmlElement("<a href=\"index.html\" />", "http://example.com");
		assertEquals("http://example.com/index.html", HCardUtils.getAbsUrl(element, "href"));

		element = toHtmlElement("<a href=\"mailto:jdoe@hotmail.com\" />", "http://example.com");
		assertEquals("mailto:jdoe@hotmail.com", HCardUtils.getAbsUrl(element, "href"));

		element = toHtmlElement("<a href=\"http://foobar.com/index.html\" />", "http://example.com");
		assertEquals("http://foobar.com/index.html", HCardUtils.getAbsUrl(element, "href"));
	}

	@Test
	public void appendTextAndEncodeNewLines() {
		Element element = toHtmlElement("<div />");
		HCardUtils.appendTextAndEncodeNewlines(element, "Append\rthis\n\ntext\r\nplease.");

		List<Node> nodes = element.childNodes();
		assertEquals(8, nodes.size());
		int i = 0;
		assertEquals("Append", ((TextNode) nodes.get(i++)).text());
		assertEquals("br", ((Element) nodes.get(i++)).tagName());
		assertEquals("this", ((TextNode) nodes.get(i++)).text());
		assertEquals("br", ((Element) nodes.get(i++)).tagName());
		assertEquals("br", ((Element) nodes.get(i++)).tagName());
		assertEquals("text", ((TextNode) nodes.get(i++)).text());
		assertEquals("br", ((Element) nodes.get(i++)).tagName());
		assertEquals("please.", ((TextNode) nodes.get(i++)).text());

		element = toHtmlElement("<div />");
		HCardUtils.appendTextAndEncodeNewlines(element, "Without newlines.");

		nodes = element.childNodes();
		assertEquals(1, nodes.size());
		i = 0;
		assertEquals("Without newlines.", ((TextNode) nodes.get(i++)).text());
	}
}
