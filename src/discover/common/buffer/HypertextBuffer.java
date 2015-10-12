package discover.common.buffer;

/**
 * @author Tony Pinkston
 */
public class HypertextBuffer extends AbstractBuffer {

    @Override
    public final boolean isHTML() { return true; }

    public void addTable(
        Integer border,
        Integer spacing,
        int rows,
        int columns,
        String data[][]) {

        super.addText("<table");

        if (border != null) {

            super.addText(" border=\"" + border.toString() + "\"");
        }

        if (spacing != null) {

            super.addText(" cellspacing=\"" + spacing.toString() + "\"");
        }

        super.addText(">");

        for(int row = 0; row < rows; ++row) {

            super.addText("<tr>");

            for(int column = 0; column < columns; ++column) {

                addText("<td valign=\"top\">");
                addText(data[row][column]);
                addText("</td>");
            }

            super.addText("</tr>");
        }

        super.addText("</table>");
    }
}
