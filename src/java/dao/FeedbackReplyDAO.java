package dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import model.FeedbackReply;

/**
 *
 * @author kanan
 */
public class FeedbackReplyDAO {

    private final Connection conn;

    public FeedbackReplyDAO(Connection conn) {
        this.conn = conn;
    }

    public int insertReply(FeedbackReply reply) throws SQLException {
        String sql = "INSERT INTO feedback_reply (feedback_id, reply_text) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, reply.getFeedbackId());
            pstmt.setString(2, reply.getReplyText());
            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    reply.setReplyId(id);
                    return id;
                } else {
                    throw new SQLException("Creating reply failed, no ID obtained.");
                }
            }
        }
    }

    public List<FeedbackReply> getAllReplies() throws SQLException {
        String query = "SELECT * FROM feedback_reply";
        List<FeedbackReply> replies = new ArrayList<>();
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                FeedbackReply reply = new FeedbackReply();
                reply.setReplyId(rs.getInt("reply_id"));
                reply.setFeedbackId(rs.getInt("feedback_id"));
                reply.setReplyText(rs.getString("reply_text"));
                reply.setReplyDate(rs.getTimestamp("reply_date").toLocalDateTime());
                replies.add(reply);
            }

        }
        return replies;
    }

    public List<FeedbackReply> getRepliesByFeedbackId(int feedbackId) throws SQLException {
        String query = "SELECT * FROM feedback_reply WHERE feedback_id = ? ORDER BY reply_date ASC";
        List<FeedbackReply> replyList = new ArrayList<>();
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, feedbackId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    FeedbackReply reply = new FeedbackReply();
                    reply.setReplyId(rs.getInt("reply_id"));
                    reply.setFeedbackId(rs.getInt("feedback_id"));
                    reply.setReplyText(rs.getString("reply_text"));
                    reply.setReplyDate(rs.getTimestamp("reply_date").toLocalDateTime());
                    replyList.add(reply);
                }
            }
        }
        return replyList;
    }
}
