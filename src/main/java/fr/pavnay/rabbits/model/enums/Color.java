package fr.pavnay.rabbits.model.enums;

/**
 * 
 * An utility enum to give color to rabbits
 *
 */
public enum Color {
    
    WHITE(java.awt.Color.WHITE),
    BROWN(new java.awt.Color(149, 86, 40));
    
    private java.awt.Color color;
    
    private Color( java.awt.Color color) {
        this.color = color;
    }
    
    public java.awt.Color getColor() {
        return color;
    }
}
