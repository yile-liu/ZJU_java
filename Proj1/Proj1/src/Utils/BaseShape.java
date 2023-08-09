package Utils;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class BaseShape implements Serializable {
    public ShapeType type;
    public int       x1, y1, x2, y2;
    public           String text;
    public           Color  color;
    transient public Stroke stroke;
    public           float  stroke_width = 1.5f;
    public           Font   font;
    final            double ratio        = 1.1;

    public BaseShape(ShapeType type, int x1, int y1) {
        this.type = type;
        this.x1   = x1;
        this.y1   = y1;
        if (type == ShapeType.LINE) {
            this.x2 = x1;
            this.y2 = y1;
        }
        this.stroke = new BasicStroke(1.0f);
        this.color  = Color.BLACK;
    }

    public BaseShape(ShapeType type, int x1, int y1, String text) {
        this.type   = type;
        this.x1     = x1;
        this.y1     = y1;
        this.text   = text;
        this.color  = Color.BLACK;
        this.stroke = new BasicStroke(1.0f);
        this.font   = new Font("微软雅黑", Font.BOLD, 15);
    }

    public void ZoomIn() {
        if (type == ShapeType.LINE) {
            x2 += (x2 - x1) * (ratio - 1);
            y2 += (y2 - y1) * (ratio - 1);
        } else if (type == ShapeType.TEXT) {
            font = new Font("微软雅黑", Font.BOLD, (int) (font.getSize() * ratio));
        } else if (type == ShapeType.REC || type == ShapeType.OVAL) {
            x2 *= ratio;
            y2 *= ratio;
        }
    }

    public void ZoomOut() {
        if (type == ShapeType.LINE) {
            x2 -= (x2 - x1) * (ratio - 1);
            y2 -= (y2 - y1) * (ratio - 1);
        } else if (type == ShapeType.TEXT) {
            font = new Font("微软雅黑", Font.BOLD, (int) (font.getSize() / ratio));
        } else if (type == ShapeType.REC || type == ShapeType.OVAL) {
            x2 /= ratio;
            y2 /= ratio;
        }
    }

    public void Bolder() {
        if (type == ShapeType.TEXT) {
            JOptionPane.showMessageDialog(null, "文本无法应用粗细，请使用大小调节。");
        } else {
            stroke_width *= ratio;
            stroke = new BasicStroke(stroke_width);
        }
    }

    public void Lighter() {
        if (type == ShapeType.TEXT) {
            JOptionPane.showMessageDialog(null, "文本无法应用粗细，请使用大小调节。");
        } else {
            stroke_width /= ratio;
            stroke = new BasicStroke(stroke_width);
        }
    }
}


