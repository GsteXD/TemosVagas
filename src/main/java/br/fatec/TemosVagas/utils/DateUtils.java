package br.fatec.TemosVagas.utils;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class DateUtils {

    private static final DateTimeFormatter formatarMesAno =
            DateTimeFormatter.ofPattern("MMMM/yyyy", new Locale("pt", "BR"));

    private DateUtils() {}

    public static boolean ehMesAtualOuPosterior(String dataString) {
        YearMonth dataInformada = converterParaAnoMes(dataString);
        if(dataInformada == null) {
            return false;
        }
        YearMonth mesAtual = YearMonth.now();
        return dataInformada.isAfter(mesAtual) || dataInformada.equals(mesAtual);
    }

    public static boolean ehMesAnterior(String dataString) {
        YearMonth dataInformada = converterParaAnoMes(dataString);
        if(dataInformada == null) {
            return false;
        }
        YearMonth mesAtual = YearMonth.now();
        return dataInformada.isBefore(mesAtual);
    }

    public static Integer extrairAno(String dataString) {
        YearMonth data = converterParaAnoMes(dataString);
        return (data != null) ? data.getYear() : null;
    }

    public static YearMonth converterParaAnoMes(String dataString) {
        if(dataString == null || dataString.isBlank()) {
            return null;
        }
        try {
            return YearMonth.parse(dataString, formatarMesAno);
        }catch (DateTimeParseException e) {
            return null;
        }
    }

}
