package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum EMITTER_FUNCTION implements EnumInterface {

    EMITTER_FUNC_OTHER(0, "Other"),
    EMITTER_FUNC_MULTI_FUNCTION(1, "Multi-function"),
    EMITTER_FUNC_EARLY_WARNING_SURVEILLANCE(2, "Early Warning/Surveillance"),
    EMITTER_FUNC_HEIGHT_FINDER(3, "Height Finder"),
    EMITTER_FUNC_FIRE_CONTROL(4, "Fire Control"),
    EMITTER_FUNC_ACQUISITION_DETECTION(5, "Acquisition/Detection"),
    EMITTER_FUNC_TRACKER(6, "Tracker"),
    EMITTER_FUNC_GUIDANCE_ILLUMINATION(7, "Guidance/Illumination"),
    EMITTER_FUNC_FIRING_POINT_LOCATION(8, "Firing point/launch point location"),
    EMITTER_FUNC_RANGE_ONLY(9, "Range Only"),
    EMITTER_FUNC_RADAR_ALTIMETER(10, "Radar Altimeter"),
    EMITTER_FUNC_IMAGING(11, "Imaging"),
    EMITTER_FUNC_MOTION_DETECTION(12, "Motion Detection"),
    EMITTER_FUNC_NAVIGATION(13, "Navigation"),
    EMITTER_FUNC_WEATHER_METEROLOGICAL(14, "Weather / Meteorological"),
    EMITTER_FUNC_INSTRUMENTATION(15, "Instrumentation"),
    EMITTER_FUNC_ID_CLASS(16, "Identification/Classification (including IFF)"),
    EMITTER_FUNC_AAA_FIRE_CONTROL(17, "AAA (Anti-Aircraft Artillery) Fire Control"),
    EMITTER_FUNC_AIR_SEARCH_BOMB(18, "Air Search / Bomb"),
    EMITTER_FUNC_AIR_INTERCEPT(19, "Air Intercept"),
    EMITTER_FUNC_ALTIMETER(20, "Altimeter"),
    EMITTER_FUNC_AIR_MAPPING(21, "Air Mapping"),
    EMITTER_FUNC_AIR_TRAFFIC_CONTROL(22, "Air Traffic Control"),
    EMITTER_FUNC_BEACON(23, "Beacon"),
    EMITTER_FUNC_BATTLEFIELD_SURVEILLANCE(24, "Battlefield Surveillance"),
    EMITTER_FUNC_GROUND_CONTROL_APPROACH(25, "Ground Control Approach"),
    EMITTER_FUNC_GROUND_CONTROL_INTERCEPT(26, "Ground Control Intercept"),
    EMITTER_FUNC_COASTAL_SURVEILLANCE(27, "Coastal Surveillance"),
    EMITTER_FUNC_DECOY_MIMIC(28, "Decoy / Mimic"),
    EMITTER_FUNC_DATA_TRANSMISSION(29, "Data Transmission"),
    EMITTER_FUNC_EARTH_SURVEILLANCE(30, "Earth Surveillance"),
    EMITTER_FUNC_GUN_LAY_BEACON(31, "Gun Lay Beacon"),
    EMITTER_FUNC_GROUND_MAPPING(32, "Ground Mapping"),
    EMITTER_FUNC_HARBOR_SURVEILLANCE(33, "Harbor Surveillance"),
    EMITTER_FUNC_ILS(35, "ILS (Instrument Landing System)"),
    EMITTER_FUNC_IONOSPHERIC_SOUND(36, "Ionospheric Sound"),
    EMITTER_FUNC_INTERROGATOR(37, "Interrogator"),
    EMITTER_FUNC_BARRAGE_JAMMER(38, "Barrage Jammer"),
    EMITTER_FUNC_CLICK_JAMMER(39, "Click Jammer"),
    EMITTER_FUNC_FREQUENCY_SWEPT_JAMMER(41, "Frequency Swept Jammer"),
    EMITTER_FUNC_JAMMER(42, "Jammer"),
    EMITTER_FUNC_PULSED_JAMMER(44, "Pulsed Jammer"),
    EMITTER_FUNC_REPEATER_JAMMER(45, "Repeater Jammer"),
    EMITTER_FUNC_SPOT_NOISE_JAMMER(46, "Spot Noise Jammer"),
    EMITTER_FUNC_MISSILE_ACQUISITION(47, "Missile Acquisition"),
    EMITTER_FUNC_MISSILE_DOWNLINK(48, "Missile Downlink"),
    EMITTER_FUNC_SPACE(50, "Space"),
    EMITTER_FUNC_SURFACE_SEARCH_(51, "Surface Search "),
    EMITTER_FUNC_SHELL_TRACKING(52, "Shell Tracking"),
    EMITTER_FUNC_TELEVISION(56, "Television"),
    EMITTER_FUNC_UNKNOWN(57, "Unknown"),
    EMITTER_FUNC_VIDEO_REMOTING(58, "Video Remoting"),
    EMITTER_FUNC_EXPERIMENTAL(59, "Experimental or Training"),
    EMITTER_FUNC_MISSILE_GUIDANCE(60, "Missile Guidance"),
    EMITTER_FUNC_MISSILE_HOMING(61, "Missile Homing"),
    EMITTER_FUNC_MISSILE_TRACKING(62, "Missile Tracking"),
    EMITTER_FUNC_JAMMER_NOISE(64, "Jammer, Noise"),
    EMITTER_FUNC_JAMMER_DECEPTION(65, "Jammer, Deception"),
    EMITTER_FUNC_NAV_DIST_MEASURING_EQUIPMENT(71, "Navigation / Distance Measuring Equipment"),
    EMITTER_FUNC_TERRAIN_FOLLOWING(72, "Terrain Following"),
    EMITTER_FUNC_WEATHER_AVOIDANCE(73, "Weather Avoidance"),
    EMITTER_FUNC_PROXIMITY_FUSE(74, "Proximity Fuse"),
    EMITTER_FUNC_RADIOSONDE(76, "Radiosonde"),
    EMITTER_FUNC_SONOBUOY(77, "Sonobuoy"),
    EMITTER_FUNC_BATHYTHERMAL_SENSOR(78, "Bathythermal Sensor"),
    EMITTER_FUNC_TOWED_COUNTER_MEASURE(79, "Towed Counter Measure"),
    EMITTER_FUNC_WEAPON_NON_LETHAL(96, "Weapon, Non-lethal"),
    EMITTER_FUNC_WEAPON_LETHAL(97, "Weapon, Lethal");

    private final int value;
    private final String description;

    private EMITTER_FUNCTION(int value, String description) {

        this.value = value;
        this.description = description;
    }

    @Override
    public int getValue() {

        return value;
    }

    @Override
    public String getName() {

        return name();
    }

    @Override
    public String getDescription() {

        return description;
    }

    public static EnumInterface getValue(int value) {

        for(EnumInterface element : values()) {

            if (element.getValue() == value) {

                return element;
            }
        }

        return Enumerations.getUnknownValue(value, EMITTER_FUNCTION.class);
    }
}

