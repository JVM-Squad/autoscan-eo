import java.nio.file.Path

/**
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

/**
 * Entry point for running validation scripts.
 * To add new validation create new script in this folder and add it
 * to the list below.
 * @todo #2684:30min Remove dependencies from eo-runtime. eo-runtime must stay clear out of any
 *  dependencies except those that are used in tests. It's needed to remove all such dependencies
 *  and include 'check-runtime-deps.groovy' test (which is failing for now) into 'tests' list. We
 *  also need to add "test" scope for all dependencies in pom.xml.
 */
Path folder = basedir.toPath().resolve("src").resolve("test").resolve("groovy")
tests = [
  'check-folders-numbering.groovy',
  'check-all-java-classes-compiled.groovy',
//   'check-runtime-deps.groovy'
]
for (it in tests) {
  def res = evaluate folder.resolve(it).toFile()
  println String.format('Verified with %s - OK. Result: %s', it, res)
}
true