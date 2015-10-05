/**
 * @author Tony Pinkston
 */
package discover.gui.tabs;

public enum TabType {

    // Add new enumerations to the end to preserve backwards compatibility
    // with previous versions that save ordinal values to file.
    CAPTURE(
        "Capture",
        "For capturing inbound PDU traffic."),
    PLAYBACK(
        "Playback",
        "For playing back captured PDUs (copied from capture tab)."),
    CFS(
        "CFS",
        "For designating external entity as CFS (handles action request" +
        "and Entity State PDU translation)."),
    ENTITY(
        "Entity",
        "For publishing Entity State PDUs for dummy external entity."),
    BUILDER(
        "Builder",
        "For building custom PDUs from scratch.");

    private final String label;
    private final String description;

    private TabType(String label, String description) {

        this.label = label;
        this.description = description;
    }

    public String getLabel() { return this.label; }
    public String getDescription() { return this.description; }
}
