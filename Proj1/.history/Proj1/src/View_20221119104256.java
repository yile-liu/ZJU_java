import Utils.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class View {
    JFrame               frame;
    CadCanvas            canvas;
    ArrayList<BaseShape> shapes;
    ShapeTypeRef         draw_type;
    JPanel               top_bar;
    JPanel               selected_bar;
    BaseShapeRef         selected_shape_ref;
    FileOp               fileOp;

    public View() {
        initFrame();
        initTopBar();
        initSelectedBar();
        frame.setVisible(true);
        selected_bar.setVisible(false);
    }

    private void initSelectedBar() {
        JButton zoom_in_btn      = new JButton("放大");
        JButton zoom_out_btn     = new JButton("缩小");
        JButton bold_btn         = new JButton("加粗");
        JButton light_btn        = new JButton("变细");
        JButton color_select_btn = new JButton("颜色选择");
        JButton delete_btn       = new JButton("删除");

        zoom_in_btn.addActionListener(e -> {
            selected_shape_ref.shape.ZoomIn();
            canvas.repaint();
        });
        zoom_out_btn.addActionListener(e -> {
            selected_shape_ref.shape.ZoomOut();
            canvas.repaint();
        });
        bold_btn.addActionListener(e -> {
            selected_shape_ref.shape.Bolder();
            canvas.repaint();
        });
        light_btn.addActionListener(e -> {
            selected_shape_ref.shape.Lighter();
            canvas.repaint();
        });
        color_select_btn.addActionListener(e -> {
            selected_shape_ref.shape.color = JColorChooser.showDialog(frame, "选择新的颜色", Color.WHITE);
            canvas.repaint();
        });
        delete_btn.addActionListener(e -> {
            shapes.remove(selected_shape_ref.shape);
            selected_shape_ref.shape = null;
            selected_bar.setVisible(false);
            canvas.repaint();
        });
        selected_bar = new JPanel();
        selected_bar.add(zoom_in_btn);
        selected_bar.add(zoom_out_btn);
        selected_bar.add(bold_btn);
        selected_bar.add(light_btn);
        selected_bar.add(color_select_btn);
        selected_bar.add(delete_btn);
        frame.add(selected_bar, BorderLayout.SOUTH);
    }

    private void initTopBar() {
        JToggleButton line_btn = new JToggleButton("直线");
        JToggleButton rec_btn  = new JToggleButton("矩形");
        JToggleButton oval_btn = new JToggleButton("椭圆");
        JToggleButton text_btn = new JToggleButton("文字");
        JButton       load_btn = new JButton("从文件读取");
        JButton       save_btn = new JButton("保存到文件");

        line_btn.addChangeListener(e -> {
            if (line_btn.isSelected()) {
                rec_btn.setSelected(false);
                oval_btn.setSelected(false);
                text_btn.setSelected(false);
                draw_type.type = ShapeType.LINE;
            } else {
                draw_type.type = ShapeType.NaS;
            }
        });
        rec_btn.addChangeListener(e -> {
            if (rec_btn.isSelected()) {
                line_btn.setSelected(false);
                oval_btn.setSelected(false);
                text_btn.setSelected(false);
                draw_type.type = ShapeType.REC;
            } else {
                draw_type.type = ShapeType.NaS;
            }
        });
        oval_btn.addChangeListener(e -> {
            if (oval_btn.isSelected()) {
                line_btn.setSelected(false);
                rec_btn.setSelected(false);
                text_btn.setSelected(false);
                draw_type.type = ShapeType.OVAL;
            } else {
                draw_type.type = ShapeType.NaS;
            }
        });
        text_btn.addChangeListener(e -> {
            if (text_btn.isSelected()) {
                line_btn.setSelected(false);
                rec_btn.setSelected(false);
                oval_btn.setSelected(false);
                draw_type.type = ShapeType.TEXT;
            } else {
                draw_type.type = ShapeType.NaS;
            }
        });
        load_btn.addActionListener(e -> {
            line_btn.setSelected(false);
            rec_btn.setSelected(false);
            oval_btn.setSelected(false);
            text_btn.setSelected(false);
            draw_type.type = ShapeType.NaS;
            fileOp.Load();
            canvas.repaint();
        });
        save_btn.addActionListener(e -> {
            line_btn.setSelected(false);
            rec_btn.setSelected(false);
            oval_btn.setSelected(false);
            text_btn.setSelected(false);
            draw_type.type = ShapeType.NaS;
            fileOp.Save();
        });

        top_bar = new JPanel();
        top_bar.add(line_btn);
        top_bar.add(rec_btn);
        top_bar.add(oval_btn);
        top_bar.add(text_btn);
        top_bar.add(save_btn);
        top_bar.add(load_btn);
        frame.add(top_bar, BorderLayout.NORTH);
    }

    private void initFrame() {
        frame  = new JFrame();
        canvas = new CadCanvas();
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(canvas);
    }

    public void setShapes(ArrayList<BaseShape> shapes) {
        this.shapes        = shapes;
        this.canvas.shapes = shapes;
    }
}

class CadCanvas extends Canvas {
    ArrayList<BaseShape> shapes;

    @Override
    public void paint(Graphics g) {
        if (shapes != null) {
            Graphics2D g2 = (Graphics2D) g;
            for (BaseShape shape : shapes) {
                g2.setColor(shape.color);
                g2.setStroke(shape.stroke);
                switch (shape.type) {
                    case LINE -> g2.drawLine(shape.x1, shape.y1, shape.x2, shape.y2);
                    case REC -> g2.drawRect(shape.x1, shape.y1, shape.x2, shape.y2);
                    case OVAL -> g2.drawOval(shape.x1, shape.y1, shape.x2, shape.y2);
                    case TEXT -> {
                        g2.setFont(shape.font);
                        g2.drawString(shape.text, shape.x1, shape.y1);
                    }
                }
            }
        }
    }
}
