package io.github.shafr.dtfexample;

import io.github.shafr.dockertestframework.DockerTest;
import io.github.shafr.dockertestframework.annotations.*;
import org.testng.Assert;
import org.testng.annotations.Test;

@Image("alpine")
@EntryPoint("/bin/sh")
@Environment("key=value")
@Environment("key2=value2")
@CommandLineArgument({"ls", "-ltrh"})
public class ClassAndMethod extends DockerTest {
    @Test()
    @CommandLineArgument({"ls", "-la"})
    public void testCommandLineArgument() {
        String[] actual = inspectContainer().cmd().toArray(new String[0]);
        Assert.assertEquals(actual[0], "ls");
        Assert.assertEquals(actual[1], "-la");
    }

    @Test
    @EntryPoint("/bin/ls")
    public void testEntryPoint() {
        String[] actual = inspectContainer().entrypoint().toArray(new String[0]);
        Assert.assertEquals(actual[0], "/bin/ls");
    }

    @Test
    @Environment("key3=value3")
    @Environment("key4=value4")
    public void testEnvironment() {
        String[] actual = inspectContainer().env().toArray(new String[0]);
        Assert.assertEquals(actual[0], "key=value");
        Assert.assertEquals(actual[1], "key2=value2");
        Assert.assertEquals(actual[2], "key3=value3");
        Assert.assertEquals(actual[3], "key4=value4");
        Assert.assertEquals(actual[4], "PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin");
    }

    @Test
    @Environment("key2=value6")
    public void testEnvironmentOverridesPrevious() {
        String[] actual = inspectContainer().env().toArray(new String[0]);
        Assert.assertEquals(actual[0], "key=value");
        Assert.assertEquals(actual[1], "key2=value2");
        Assert.assertEquals(actual[2], "key2=value6"); //This is odd though, maybe we should handle this
        Assert.assertEquals(actual[3], "PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin");
    }


    @Test
    public void testImage() {
        String actual = inspectContainer().image();
        Assert.assertEquals(actual, "alpine");
    }
}
