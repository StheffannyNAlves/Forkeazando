
import org.jline.keymap.BindingReader;
import org.jline.keymap.KeyMap;
import org.jline.terminal.*;

import java.io.IOException;

public class TesteJLine {

    public static void main(String[] args) throws IOException {

        try (Terminal t = TerminalBuilder.builder()
                .system(true)
                .build()) {

            System.out.println("Type: " + t.getType());

            t.enterRawMode();

            BindingReader bindingReader =
                    new BindingReader(t.reader());

            KeyMap<String> map = new KeyMap<>();

            map.bind("UP", "\033[A");
            map.bind("DOWN", "\033[B");
            map.bind("RIGHT", "\033[C");
            map.bind("LEFT", "\033[D");

            map.bind("ENTER", "\r");
            map.bind("ENTER", "\n");

            map.bind("EXIT", "\003");

            map.setAmbiguousTimeout(150);

            try {
                while (true) {

                    System.out.print("> ");
                    System.out.flush();

                    String tecla = bindingReader.readBinding(map);

                    if (tecla == null) {
                        System.out.println("Nenhum comando reconhecido.");
                        continue;
                    }

                    System.out.println("Tecla: " + tecla);

                    if (tecla.equals("EXIT")) {
                        break;
                    }
                }

            } finally {
                t.writer().println();
                t.writer().flush();
            }
        }
    }
}