package ru.kulikov.first_bot.command_classes;

import ru.kulikov.first_bot.Bot;
import ru.kulikov.first_bot.service_classes.MapUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class ListExpenses {

    public static String listExpenses() throws SQLException {
        StringBuilder answer = new StringBuilder();
        Map<String, Integer> map = new HashMap<>();
        int sum = 0;
        Statement statement = Bot.connection.createStatement();
        String SQL = "SELECT * FROM sum WHERE chat_id=" + Bot.user_id;
        ResultSet resultSet = statement.executeQuery(SQL);
        while (resultSet.next()) {
            String category = resultSet.getString("category");
            int local_sum = resultSet.getInt("local_sum");
            map.put(category, local_sum);
            sum += local_sum;
        }
        map = MapUtil.sortByValue(map);
        for (String key : map.keySet()) {
            answer.append(key).append(" - ").append(map.get(key)).append("\n");
        }
        answer.append("\n<b>ВСЕГО - ").append(sum).append("</b>");
        return answer.toString();
    }
}
