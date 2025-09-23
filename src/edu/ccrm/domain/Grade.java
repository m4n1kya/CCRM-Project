package edu.ccrm.domain;

public enum Grade {
    S(10.0), A(9.0), B(8.0), C(7.0), D(6.0), E(5.0), F(0.0), NOT_GRADED(0.0);
    
    private final double points;
    
    Grade(double points) {
        this.points = points;
    }
    
    public double getPoints() {
        return points;
    }
    
    public static Grade fromPercentage(double percentage) {
        if (percentage >= 90) return S;
        if (percentage >= 80) return A;
        if (percentage >= 70) return B;
        if (percentage >= 60) return C;
        if (percentage >= 50) return D;
        if (percentage >= 40) return E;
        return F;
    }
}