public class MiniCAD {
    View       view;
    Controller controller;
    Model      model;

    public void init() {
        model      = new Model();
        view       = new View();
        controller = new Controller();

        view.setShapes(model.shapes);
        controller.shapes = model.shapes;
        controller.fileIO = model;

        view.fileOp             = controller;
        view.draw_type          = controller.draw_type;
        view.selected_shape_ref = controller.shape_selected_ref;

        controller.canvas       = view.canvas;
        controller.selected_bar = view.selected_bar;

        view.canvas.addMouseListener(controller);
        view.canvas.addMouseMotionListener(controller);
    }

}

