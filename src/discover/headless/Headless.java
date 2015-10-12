package discover.headless;

import java.text.DateFormat;
import java.util.List;
import java.util.StringTokenizer;

import discover.common.buffer.PlainTextBuffer;
import discover.system.CaptureThread;
import discover.system.CaptureThreadListener;
import discover.vdis.PDU;

/**
 * @author Tony Pinkston
 */
public class Headless {

    private static final DateFormat format = DateFormat.getDateTimeInstance();

    private static Integer port = null;

    private static Integer exercise = null;

    private static Integer pdutype = null;

    private static Integer pdufamily = null;

    private static boolean verbose = false;

    /**
     * Arguments should contain no whitespace and be in the form:
     * "arg1:value1,arg2:value2,arg3:value3"
     *
     * @param arguments
     */
    public static void run(String arguments) {

        StringTokenizer tokenizer = new StringTokenizer(arguments, ",");

        while (tokenizer.hasMoreTokens()) {

            String parameter = tokenizer.nextToken();

            if (parameter.startsWith("port:")) {

                String value = parameter.replace("port:", "");

                try {

                    port = Integer.parseInt(value);
                }
                catch (NumberFormatException exception) {

                    System.err.println("Invalid port: '" + value + "'");
                    System.exit(0);
                }
            }
            else if (parameter.startsWith("exercise:")) {

                String value = parameter.replace("exercise:", "");

                try {

                    exercise = Integer.parseInt(value);
                }
                catch (NumberFormatException exception) {

                    System.err.println("Invalid exercise: '" + value + "'");
                    System.exit(0);
                }
            }
            else if (parameter.startsWith("pdutype:")) {

                String value = parameter.replace("pdutype:", "");

                try {

                    pdutype = Integer.parseInt(value);
                }
                catch (NumberFormatException exception) {

                    System.err.println("Invalid PDU type: '" + value + "'");
                    System.exit(0);
                }
            }
            else if (parameter.startsWith("pdufamily:")) {

                String value = parameter.replace("pdufamily:", "");

                try {

                    pdufamily = Integer.parseInt(value);
                }
                catch (NumberFormatException exception) {

                    System.err.println("Invalid PDU family: '" + value + "'");
                    System.exit(0);
                }
            }
            else if (parameter.startsWith("verbose:")) {

                String value = parameter.replace("verbose:", "");

                if (value.startsWith("Y") || value.startsWith("y")) {

                    verbose = true;
                }
            }
            else {

                System.err.println("Invalid headless parameter: '" + parameter + "'");
                System.exit(0);
            }
        }

        if (port == null) {

            System.err.println("Port request to run headless!");
            System.exit(0);
        }

        if (pdutype != null) {

        }

        if (pdufamily != null) {

        }

        try {

            run();
        }
        catch (Exception exception) {

            exception.printStackTrace();
            System.exit(0);
        }
    }

    static void run() throws Exception {

        CaptureThreadListener listener = new Listener();

        CaptureThread thread = new CaptureThread(
            ("headless_capture:" + port.toString()),
            listener,
            port.intValue());

        System.out.println(
            "Running headless on port " + port +
                ", press <ctrl-c> to stop...");

        // No need to start new start since we're running headess, just run
        // the thread's main method.
        //
        thread.run();
    }

    static class Listener implements CaptureThreadListener {

        @Override
        public void pdusCaptured(List<PDU> list) {

            for (PDU pdu : list) {

                boolean process = true;

                if (process && (exercise != null)) {

                    process = (pdu.getExercise() == exercise.intValue());
                }

                if (process && (pdutype != null)) {

                    process = (pdu.getType() == pdutype.intValue());
                }

                if (process) {

                    processPDU(pdu);
                }
            }
        }

        private void processPDU(PDU pdu) {

            StringBuilder string = new StringBuilder();

            if (verbose) {

                PlainTextBuffer buffer = new PlainTextBuffer();

                pdu.toBuffer(buffer);

                string.append(buffer.toString());
            }
            else {

                string.append(format.format(pdu.getTime()));
                string.append(": ex: ");
                string.append(pdu.getExercise());
                string.append(", ");
                string.append(pdu.getTimestamp());
                string.append(", ");
                string.append(pdu.getSource());
                string.append(", ");
                string.append(pdu.getTitle());
                string.append(", ");
                string.append(pdu.getLength());
                string.append(" bytes");

                if (pdu.getId() != null) {

                    string.append(", from: ");
                    string.append(pdu.getId().toHeadlessString());
                }

                if (pdu.getRecipient() != null) {

                    string.append(", to: ");
                    string.append(pdu.getRecipient().toHeadlessString());
                }

                if (pdu.hasRequestId()) {

                    string.append(", reqid: ");
                    string.append(pdu.getRequestId());
                }
            }

            System.out.println(string.toString());
            System.out.flush();
        }
    }
}
