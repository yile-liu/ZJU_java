import Utils.*;

import javax.swing.*;
import javax.swing.event.MouseInputListener;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

class Controller implements MouseInputListener, FileOp {
    ShapeTypeRef         draw_type;
    ArrayList<BaseShape> shapes;
    CadCanvas            canvas;
    BaseShape            shape_drawing;
    BaseShape            shape_dragged;
    BaseShapeRef         shape_selected_ref;
    JPanel               selected_bar;
    int                  prev_x, prev_y;
    FileIO fileIO;

    Controller() {
        draw_type          = new ShapeTypeRef();
        shape_selected_ref = new BaseShapeRef();
    }

    private void selectShape(int x, int y) {
        BaseShape shape_found = null;
        for (BaseShape shape : shapes) {
            switch (shape.type) {
                case LINE:
                    // line的x2、y2是绝对坐标，其余都是相对x1、y1的长宽
                    int left_x = Math.min(shape.x1, shape.x2);
                    int right_x = Math.max(shape.x1, shape.x2);
                    int top_y = Math.min(shape.y1, shape.y2);
                    int butt_y = Math.max(shape.y1, shape.y2);
                    if (x >= left_x && x <= right_x && y >= top_y && y <= butt_y) {
                        shape_found = shape;
                    }
                    break;
                case TEXT:
                    FontMetrics metrics = new JLabel().getFontMetrics(shape.font);
                    shape.x2 = metrics.stringWidth(shape.text);
                    shape.y2 = metrics.getHeight();
                    // text的x1、y1是左下角，其余都是左上角
                    if (x >= shape.x1 && x <= shape.x1 + shape.x2 && y <= shape.y1 && y >= shape.y1 - shape.y2) {
                        shape_found = shape;
                    }
                    break;
                case REC:
                case OVAL:
                    if (x >= shape.x1 && x <= shape.x1 + shape.x2 && y >= shape.y1 && y <= shape.y1 + shape.y2) {
                        shape_found = shape;
                    }
                    break;
            }
            if (shape_found != null) {
                shape_dragged = shape_found;
                break;
            }
        }
        shape_selected_ref.shape = shape_found;
        selected_bar.setVisible(shape_found != null);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        prev_x = e.getX();
        prev_y = e.getY();
        if (draw_type.type == ShapeType.NaS) {
            selectShape(e.getX(), e.getY());
        } else {
            switch (draw_type.type) {
                case LINE -> shape_drawing = new BaseShape(ShapeType.LINE, e.getX(), e.getY());
                case REC -> shape_drawing = new BaseShape(ShapeType.REC, e.getX(), e.getY());
                case OVAL -> shape_drawing = new BaseShape(ShapeType.OVAL, e.getX(), e.getY());
                case TEXT -> {
                    String text = JOptionPane.showInputDialog("请输入文本：");
                    if (text != null) {
                        if (!text.strip().equals("")) {
                            // 输入文本没有release 不能用shape_drawing
                            shapes.add(new BaseShape(ShapeType.TEXT, e.getX(), e.getY(), text.strip()));
                        }
                    }
                }
            }

        }
        if (shape_drawing != null) {
            shapes.add(shape_drawing);
        }
        canvas.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {

        if (shape_drawing != null) {
            boolean invalid_shape = false;
            if (shape_drawing.type == ShapeType.LINE) {
                if (shape_drawing.x1 == shape_drawing.x2 && shape_drawing.y1 == shape_drawing.y2) {
                    invalid_shape = true;
                }
            } else if (shape_drawing.type == ShapeType.REC || shape_drawing.type == ShapeType.OVAL) {
                if (shape_drawing.x2 <= 0 || shape_drawing.y2 <= 0) {
                    invalid_shape = true;
                }
            }
            if (!invalid_shape) {
                shape_selected_ref.shape = shape_drawing;
                selected_bar.setVisible(true);
            } else {
                shapes.remove(shape_drawing);
                selected_bar.setVisible(false);
            }
        }

        shape_drawing = null;
        shape_dragged = null;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        System.out.println("Drag: " + e.getPoint());
        if (shape_drawing != null) {
            if (shape_drawing.type == ShapeType.LINE) {
                shape_drawing.x2 = e.getX();
                shape_drawing.y2 = e.getY();
            } else {
                shape_drawing.x2 = e.getX() - shape_drawing.x1;
                shape_drawing.y2 = e.getY() - shape_drawing.y1;
            }
        } else if (shape_dragged != null) {
            if (shape_dragged.type == ShapeType.LINE) {
                shape_dragged.x1 += e.getX() - prev_x;
                shape_dragged.y1 += e.getY() - prev_y;
                shape_dragged.x2 += e.getX() - prev_x;
                shape_dragged.y2 += e.getY() - prev_y;
            } else {
                shape_dragged.x1 += e.getX() - prev_x;
                shape_dragged.y1 += e.getY() - prev_y;
            }
        }
        prev_x = e.getX();
        prev_y = e.getY();
        canvas.repaint();
    }

    @Override
    public void Save() {
        String file_name = JOptionPane.showInputDialog("请输入文件名：");
        if (file_name != null) {
            try {
                fileIO.SaveFile(file_name);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "保存时发生错误");
            }
        }
    }

    @Override
    public void Load() {
        String file_name = JOptionPane.showInputDialog("请输入文件名：");
        if (file_name != null) {
            try {
                fileIO.LoadFile(file_name);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "文件不存在或文件内容错误");
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }
}