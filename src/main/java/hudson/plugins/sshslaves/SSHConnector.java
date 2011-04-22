package hudson.plugins.sshslaves;

import hudson.Extension;
import hudson.model.TaskListener;
import hudson.slaves.ComputerConnector;
import hudson.slaves.ComputerConnectorDescriptor;
import hudson.util.Secret;
import java.io.IOException;
import org.kohsuke.stapler.DataBoundConstructor;

import static hudson.Util.fixEmpty;

/**
 * {@link ComputerConnector} for {@link SSHLauncher}.
 * <p/>
 * <p/>
 * Significant code duplication between this and {@link SSHLauncher} because of the historical reason.
 * Newer plugins like this should define a separate Describable connection parameter class and have
 * connector and launcher share them.
 *
 * @author Kohsuke Kawaguchi
 */
public class SSHConnector extends ComputerConnector {
    /**
     * Field port
     */
    public final int port;

    /**
     * Field username
     */
    public final String username;

    /**
     * Field password
     *
     * todo remove password once authentication is stored in the descriptor.
     */
    public final Secret password;

    /**
     * File path of the private key.
     */
    public final String privatekey;

    /**
     * Field javaPath.
     */
    public final String javaPath;

    /**
     * Field jvmOptions.
     */
    public final String jvmOptions;

    /**
     * Constructor SSHLauncher creates a new SSHLauncher instance.
     *
     * @param port The port to connect on.
     * @param username The username to connect as.
     * @param password The password to connect with.
     * @param privatekey The ssh privatekey to connect with.
     * @param jvmOptions jvm options.
     * @param javaPath optional path to java.
     */
    @DataBoundConstructor
    public SSHConnector(int port, String username, String password, String privatekey, String jvmOptions,
                        String javaPath) {
        this.jvmOptions = jvmOptions;
        this.port = port == 0 ? 22 : port;
        this.username = username;
        this.password = Secret.fromString(fixEmpty(password));
        this.privatekey = privatekey;
        this.javaPath = javaPath;
    }

    @Override
    public SSHLauncher launch(String host, TaskListener listener) throws IOException, InterruptedException {
        return new SSHLauncher(host, port, username, Secret.toString(password), privatekey, jvmOptions, javaPath);
    }

    @Extension
    public static class DescriptorImpl extends ComputerConnectorDescriptor {
        @Override
        public String getDisplayName() {
            return Messages.SSHLauncher_DescriptorDisplayName();
        }
    }
}
