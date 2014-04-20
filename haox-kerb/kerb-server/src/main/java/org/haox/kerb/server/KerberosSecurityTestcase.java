package org.haox.kerb.server;

import org.junit.After;
import org.junit.Before;

import java.io.File;
import java.util.Properties;

/**
 * KerberosSecurityTestcase provides a base class for using TestKdcServer with other
 * testcases. KerberosSecurityTestcase starts the TestKdcServer (@Before) before
 * running tests, and stop the TestKdcServer (@After) after the testcases, using
 * default settings (working dir and kdc configurations).
 * <p>
 * Users can directly inherit this class and implement their own test functions
 * using the default settings, or override functions getTestDir() and
 * createMiniKdcConf() to provide new settings.
 *
 */

public class KerberosSecurityTestcase {
  private MiniKdc kdc;
  private File workDir;
  private Properties conf;

  @Before
  public void startMiniKdc() throws Exception {
    createTestDir();
    createMiniKdcConf();

    kdc = new MiniKdc(conf, workDir);
    kdc.start();
  }

  /**
   * Create a working directory, it should be the build directory. Under
   * this directory an ApacheDS working directory will be created, this
   * directory will be deleted when the TestKdcServer stops.
   */
  public void createTestDir() {
    workDir = new File(System.getProperty("test.dir", "target"));
  }

  /**
   * Create a Kdc configuration
   */
  public void createMiniKdcConf() {
    conf = MiniKdc.createConf();
  }

  @After
  public void stopMiniKdc() {
    if (kdc != null) {
      kdc.stop();
    }
  }

  public MiniKdc getKdc() {
    return kdc;
  }

  public File getWorkDir() {
    return workDir;
  }

  public Properties getConf() {
    return conf;
  }
}
