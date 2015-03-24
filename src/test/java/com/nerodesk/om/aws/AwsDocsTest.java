/**
 * Copyright (c) 2015, nerodesk.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met: 1) Redistributions of source code must retain the above
 * copyright notice, this list of conditions and the following
 * disclaimer. 2) Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided
 * with the distribution. 3) Neither the name of the nerodesk.com nor
 * the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.nerodesk.om.aws;

import com.google.common.collect.Lists;
import com.jcabi.s3.Bucket;
import com.jcabi.s3.mock.MkBucket;
import com.nerodesk.om.Docs;
import java.io.File;
import java.io.IOException;
import java.util.List;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Tests for {@link AwsDocs}.
 *
 * @author Krzysztof Krason (Krzysztof.Krason@gmail.com)
 * @version $Id$
 * @since 0.3
 */
public final class AwsDocsTest {

    /**
     * AwsDocs conforms to equals and hashCode contract.
     */
    @Test
    public void conformsToEqualsHashCodeContract() {
        EqualsVerifier.forClass(AwsDocs.class)
            .suppress(Warning.TRANSIENT_FIELDS)
            .verify();
    }

    /**
     * AwsDocs can list all docs.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void listsDocs() throws IOException {
        final Bucket bucket = this.mockBucket("lists", "lists-exists");
        final List<String> expected = Lists.newArrayList(bucket.list(""));
        MatcherAssert.assertThat(
            new AwsDocs(bucket).names(),
            Matchers.equalTo(expected)
        );
    }

    /**
     * AwsDocs can find a doc.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void findsDoc() throws IOException {
        final String exists = "finds-exists";
        final Bucket bucket = this.mockBucket("finds", exists);
        final Docs docs = new AwsDocs(bucket);
        MatcherAssert.assertThat(
            docs.doc(exists).exists(),
            Matchers.equalTo(true)
        );
        MatcherAssert.assertThat(
            docs.doc("xyz").exists(),
            Matchers.equalTo(false)
        );
    }

    /**
     * Builds a mock Bucket.
     * @param name Bucket name.
     * @param exists Name of Ocket that exists.
     * @return The mock bucket.
     * @throws IOException If something goes wrong.
     */
    private Bucket mockBucket(final String name, final String exists)
        throws IOException {
        final TemporaryFolder folder = new TemporaryFolder();
        folder.create();
        final File sub = new File(folder.getRoot(), name);
        assert sub.mkdir();
        assert new File(sub, exists).createNewFile();
        return new MkBucket(folder.getRoot(), name);
    }

}
