import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/Assignment2")
public class Assignment2 extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("index.html");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int coffee = getInt(request, "coffee");
        double sleep = getDouble(request, "sleep");
        int backlog = getInt(request, "backlog");
        int leetcode = getInt(request, "leetcode");
        int assessment = getInt(request, "assessment");

        int survival = calculateSurvival(coffee, sleep, backlog, leetcode, assessment);

        showResult(response, coffee, sleep, backlog, leetcode, assessment, survival);
    }

    // ---------------- CORE LOGIC ----------------

    private int calculateSurvival(int coffee, double sleep, int backlog, int leetcode, int assessment) {
        double score = 45;

        if (sleep >= 7 && sleep <= 8) score += 22;
        else if (sleep >= 6 && sleep <= 9) score += 14;
        else score -= 8;

        if (coffee >= 1 && coffee <= 3) score += 8;
        else if (coffee > 5) score -= Math.min(15, (coffee - 5) * 3);

        score -= Math.min(30, backlog * 6);
        score += Math.min(18, leetcode / 12.0);
        score += Math.min(20, assessment * 4);

        return (int) Math.max(1, Math.min(100, Math.round(score)));
    }

    // ---------------- RESPONSE HTML ----------------

    private void showResult(HttpServletResponse response,
                            int coffee,
                            double sleep,
                            int backlog,
                            int leetcode,
                            int assessment,
                            int survival) throws IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String color = getColor(survival);

        out.println("<html><head><title>Result</title>");
        out.println("<style>");
        out.println("body{background:#0f172a;color:white;font-family:Segoe UI;text-align:center;padding:40px;}");
        out.println(".box{background:#1e293b;padding:30px;border-radius:20px;display:inline-block;}");
        out.println(".score{font-size:60px;color:" + color + ";}");
        out.println("</style>");
        out.println("</head><body>");

        out.println("<div class='box'>");
        out.println("<h2>Survival Score</h2>");
        out.println("<div class='score'>" + survival + "%</div>");
        out.println("<p>Status: " + getStatus(survival) + "</p>");

        out.println("<hr>");
        out.println("<p>Coffee: " + coffee + "</p>");
        out.println("<p>Sleep: " + sleep + "</p>");
        out.println("<p>Backlogs: " + backlog + "</p>");
        out.println("<p>LeetCode: " + leetcode + "</p>");
        out.println("<p>Assessment: " + assessment + "</p>");

        out.println("<br><a href='index.html' style='color:#38bdf8'>Try Again</a>");
        out.println("</div>");

        out.println("</body></html>");
    }

    // ---------------- HELPERS ----------------

    private int getInt(HttpServletRequest request, String name) {
        try {
            return Integer.parseInt(request.getParameter(name));
        } catch (Exception e) {
            return 0;
        }
    }

    private double getDouble(HttpServletRequest request, String name) {
        try {
            return Double.parseDouble(request.getParameter(name));
        } catch (Exception e) {
            return 0;
        }
    }

    private String getStatus(int s) {
        if (s >= 80) return "Legend 👑";
        if (s >= 60) return "Stable 👍";
        if (s >= 40) return "Warning ⚠️";
        return "Critical 🚨";
    }

    private String getColor(int s) {
        if (s >= 80) return "#10b981";
        if (s >= 60) return "#3b82f6";
        if (s >= 40) return "#f59e0b";
        return "#ef4444";
    }
}