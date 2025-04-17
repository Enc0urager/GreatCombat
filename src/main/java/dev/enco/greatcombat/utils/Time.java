package dev.enco.greatcombat.utils;

import dev.enco.greatcombat.config.ConfigManager;
import dev.enco.greatcombat.config.settings.TimeFormats;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Time {
    private static final TimeFormats secondsForms = ConfigManager.getSecondsFormats();
    private static final TimeFormats minutesForms = ConfigManager.getMinutesFormats();
    private static final TimeFormats hoursForms = ConfigManager.getHoursFormats();

    public String format(int sec) {
        if (sec <= 0) return "1 " + secondsForms.form1();
        int h = sec / 3600;
        int min = (sec % 3600) / 60;
        int s = sec % 60;

        var sb = new StringBuilder();

        appendTimeUnit(sb, h, hoursForms.form1(), hoursForms.form3(), hoursForms.form5());
        appendTimeUnit(sb, min, minutesForms.form1(), minutesForms.form3(), minutesForms.form5());
        appendTimeUnit(sb, s, secondsForms.form1(), secondsForms.form3(), secondsForms.form5());

        return sb.toString().trim();
    }

    private void appendTimeUnit(StringBuilder sb, int value, String form1, String form3, String form5) {
        if (value > 0)
            sb.append(String.format("%d %s ", value, getCorrectForm(value, form1, form3, form5)));
    }

    private String getCorrectForm(int value, String form1, String form3, String form5) {
        int abs = Math.abs(value);
        int r10 = abs % 10;
        int r100 = abs % 100;

        if (r10 == 1 && r100 != 11) {
            return form1;
        } else if (r10 >= 2 && r10 <= 4 && (r100 < 10 || r100 >= 20)) {
            return form3;
        } else {
            return form5;
        }
    }
}
