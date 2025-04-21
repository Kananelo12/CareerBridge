package dao;

import model.DayCount;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kanan
 */
public class DashboardDAO {

    private final Connection conn;

    public DashboardDAO(Connection conn) {
        this.conn = conn;
    }

    public int getTotalInternships() throws SQLException {
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM internship")) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    public int getTotalApplications() throws SQLException {
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM application")) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    public int getPendingApplications() throws SQLException {
        try (PreparedStatement pst = conn.prepareStatement(
                "SELECT COUNT(*) FROM application WHERE status = ?")) {
            pst.setString(1, "pending");
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    public List<DayCount> getDailyApplicationCounts(int days) throws SQLException {
        String sql = """
            SELECT DATE(application_date) as d, COUNT(*) as c
            FROM application
            WHERE application_date >= CURDATE() - INTERVAL ? DAY
            GROUP BY DATE(application_date)
            ORDER BY d
            """;
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, days - 1);
            try (ResultSet rs = pst.executeQuery()) {
                var list = new ArrayList<DayCount>();
                while (rs.next()) {
                    LocalDate d = rs.getDate("d").toLocalDate();
                    int c = rs.getInt("c");
                    list.add(new DayCount(d, c));
                }
                return list;
            }
        }
    }
}
