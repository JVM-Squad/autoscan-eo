/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2023 Objectionary.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.eolang.maven;

import com.jcabi.log.Logger;
import com.jcabi.xml.XMLDocument;
import java.io.IOException;
import java.nio.file.Path;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.eolang.maven.latex.LatexTemplate;
import org.eolang.maven.tojos.ForeignTojo;
import org.eolang.maven.util.HmBase;

/**
 * Take .xmir files from target/eo/03-optimize directory and
 * generate .tex files for each of them in target/eo/latex directory.
 *
 * @since 0.29
 * @todo #1206:30min Create a summary file "target/eo/universe.tex".
 *  According to the <a href="https://github.com/objectionary/eo/issues/1206">issue</a>
 *  we need to generate summary in universe.tex file,
 *  which will include all generated objects. And this file
 *  should be a standalone compilable document.
 */
@Mojo(
    name = "latex",
    defaultPhase = LifecyclePhase.PROCESS_SOURCES,
    threadSafe = true
)
public final class LatexMojo extends SafeMojo {

    /**
     * The directory where to generate to.
     */
    public static final String DIR = "latex";

    /**
     * Latex extension (.tex).
     */
    public static final String EXT = "tex";

    /**
     * Truncated the last part of the filename,
     * divided by dot.
     *
     * @param input Given string.
     * @return Last string after dot.
     */
    static String last(final String input) {
        final int index = input.lastIndexOf('.');
        final String last;
        if (index == -1 || index == input.length() - 1) {
            last = input;
        } else {
            last = input.substring(index + 1);
        }
        return last;
    }

    @Override
    void exec() throws IOException {
        for (final ForeignTojo tojo : this.scopedTojos().withOptimized()) {
            final Path file = tojo.shaken();
            final Place place = new Place(
                LatexMojo.last(new XMLDocument(file).xpath("/program/@name").get(0))
            );
            final Path dir = this.targetDir.toPath();
            final Path target = place.make(
                dir.resolve(LatexMojo.DIR), LatexMojo.EXT
            );
            new HmBase(dir).save(
                new LatexTemplate(
                    new XMLDocument(file).nodes("/program/listing").get(0).toString()
                ).asString(),
                dir.relativize(target)
            );
            Logger.info(
                this,
                "Generated by LatexMojo %s file", target
            );
        }
    }

}