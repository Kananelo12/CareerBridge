package service;

import dao.DashboardDAO;
import model.DayCount;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author kanan
 */
public class DashboardService {

    private final DashboardDAO dao;

    public DashboardService(Connection conn) {
        this.dao = new DashboardDAO(conn);
    }

    public int getTotalInternships() throws SQLException {
        return dao.getTotalInternships();
    }

    public int getTotalApplications() throws SQLException {
        return dao.getTotalApplications();
    }

    public int getPendingApplications() throws SQLException {
        return dao.getPendingApplications();
    }

    public List<DayCount> getDailyApplicationCounts(int days) throws SQLException {
        return dao.getDailyApplicationCounts(days);
    }
}
