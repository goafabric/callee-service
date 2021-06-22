package org.goafabric.calleeservice.crossfunctional;

import org.springframework.boot.actuate.management.HeapDumpWebEndpoint;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

//Class that makes HeapDump Endpoint work with OpenJ9, original code is from: https://github.com/sa1nt/ibm-heapdump-spring-boot-actuator
@Component
public class OpenJ9HeapDumpWebEndpoint extends HeapDumpWebEndpoint {
    @Override
    protected HeapDumper createHeapDumper() throws HeapDumperUnavailableException {
        try {
            final Method dumpMethod = ReflectionUtils.findMethod(
                    ClassUtils.forName("com.ibm.jvm.Dump", null), "heapDumpToFile", String.class);
            return (file, live) -> ReflectionUtils.invokeMethod(dumpMethod, null, file.getAbsolutePath());
        } catch (ClassNotFoundException e) { //if openj9 is not available create the normal HeapDumper
            return super.createHeapDumper();
        }
    }
}