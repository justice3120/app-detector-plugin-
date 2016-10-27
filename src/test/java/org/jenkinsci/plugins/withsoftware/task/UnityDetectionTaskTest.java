package org.jenkinsci.plugins.withsoftware.task;

import mockit.Expectations;
import mockit.Mocked;
import org.jenkinsci.plugins.withsoftware.util.Utils;
import org.junit.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Set;

public class UnityDetectionTaskTest {

  private UnityDetectionTask task;

  @Mocked
  final Utils utils = null;

  @Before
  public void init() {
    task = new UnityDetectionTask();
  }

  @Test
  public void call() throws Exception {

    new Expectations() {{
      Utils.runExternalCommand("uname");
      result = "Darwin\n";

      Utils.runExternalCommand("ls", "/Applications/", "|", "egrep", "^Unity");
      result = "Unity\n";

      Utils.runExternalCommand("/usr/libexec/PlistBuddy", "-c", "Print :CFBundleVersion",
          "/Applications/Unity/Unity.app/Contents/Info.plist");
      result = "5.3.5f1\n";
    }};

    Set<String> labels = task.call();
    String expectedItem = "Unity:5.3.5f1:/Applications/Unity/Unity.app";

    assertThat(labels, hasItem(expectedItem));
  }
}
