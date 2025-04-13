package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Document;

/**
 *
 * @author kanan
 */
public class DocumentDAO {
    
    private Connection conn;

    public DocumentDAO(Connection conn) {
        this.conn = conn;
    }

    public void insertDocument(Document document) throws SQLException {
        String query = "INSERT INTO document (user_id, documentType, documentUrl) VALUES (?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, document.getUserId());
        pstmt.setString(2, document.getDocumentType());
        pstmt.setString(3, document.getDocumentUrl());
        pstmt.executeUpdate();
        pstmt.close();
    }
    
    public Document getDocumentByUserId(int userId) throws SQLException {
        String query = "SELECT * FROM document WHERE user_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, userId);
        ResultSet rs = pstmt.executeQuery();
        Document document = null;
        if (rs.next()) {
            document = new Document();
            document.setDocumentId(rs.getInt("document_id"));
            document.setUserId(userId);
            document.setDocumentType(rs.getString("documentType"));
            document.setDocumentUrl(rs.getString("documentUrl"));
        }
        return document;
    }
}
