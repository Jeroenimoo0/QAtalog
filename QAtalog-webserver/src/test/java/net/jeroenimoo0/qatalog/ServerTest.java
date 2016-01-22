package net.jeroenimoo0.qatalog;

import junit.framework.Assert;

import org.junit.Test;

public class ServerTest extends QAtalogTest {
	@Test
	public void testSeverStartAndStop() {
		Assert.assertTrue(QAtalog.start(DATABASE));
		QAtalog.stop();
	}
}
