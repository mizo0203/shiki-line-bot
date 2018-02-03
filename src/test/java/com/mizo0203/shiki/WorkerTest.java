/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mizo0203.shiki;

import com.linecorp.bot.model.event.JoinEvent;
import com.mizo0203.shiki.domain.UseCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

/** Unit tests for {@link Worker}. */
@RunWith(JUnit4.class)
public class WorkerTest {
  private static final String FAKE_KEY_VALUE = "KEY";
  private static final PrintStream REAL_ERR = System.err;
  // To capture and restore stderr
  private final ByteArrayOutputStream stderr = new ByteArrayOutputStream();
  @Mock private HttpServletRequest mockRequest;
  @Mock private HttpServletResponse mockResponse;
  private Worker servletUnderTest;

  @Before
  public void setUp() throws Exception {
    //  Capture stderr to examine messages written to it
    System.setErr(new PrintStream(stderr));

    MockitoAnnotations.initMocks(this);

    when(mockRequest.getParameter("key")).thenReturn(FAKE_KEY_VALUE);

    servletUnderTest = new Worker();
  }

  @After
  public void tearDown() {
    //  Restore stderr
    System.setErr(WorkerTest.REAL_ERR);
  }

  @Test
  public void doPost_writesResponse() throws Exception {
    servletUnderTest.doPost(mockRequest, mockResponse);

    String out = stderr.toString();
    // We expect a log message to be created
    // with the following message.
    assertThat(out).contains("Worker is processing " + FAKE_KEY_VALUE);
  }
}
