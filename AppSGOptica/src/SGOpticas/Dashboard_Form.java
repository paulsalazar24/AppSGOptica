package SGOpticas;


import java.awt.Color;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jose B
 */
public class Dashboard_Form extends javax.swing.JFrame {

    /**
     * Crea un nuevo formulario Dashboard _ Form
     */
    
    private int filaSeleccionada = -1; // Variable para guardar la fila seleccionada
    private boolean modoEdicion = false; // Variable para indicar si se está editando
   
    private String userRole; // Variable para almacenar el rol del usuario

    // Borde predeterminado para los elementos del menú
    Border default_border = BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(0,102,102));
        
    // Borde amarillo para los elementos del menú
    Border yellow_border = BorderFactory.createMatteBorder(1, 0, 1, 0, Color.YELLOW);
    
    // Crea una matriz de jlabels
    JLabel[] menuLabels = new JLabel[9];
    
    // Crea una matriz de jpanels
    JPanel[] panels = new JPanel[9];
    
    
    
    private Object variableGlobalIdUsuario;
    
   


    
    public Dashboard_Form() {
              
        
      initComponents();
          
        // Centra este formulario
        this.setLocationRelativeTo(null);
        
    // this.setResizable(false);
    
    
    // Bloquea el campo jTextField_IdA después de inicializar todos los componentes
        jTextField_IdA.setEditable(false); 
        jTextField_IdPa.setEditable(false);
        
   // Bloquear el campo jTextField_idProduc
        jTextField_idProduc.setEditable(false);
      
    
        
    // this.setResizable(false);
     
    jCombo_Servicio.addItemListener((ItemEvent e) -> {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            String nombreServicio = (String) e.getItem();
                try (Connection conexion = conectarDB();
                    PreparedStatement stmtServicio = conexion.prepareStatement(
                        "SELECT id, precio FROM Servicios WHERE nombre_servicio = ?")) {
                        stmtServicio.setString(1, nombreServicio);
                        ResultSet rsServicio = stmtServicio.executeQuery();
                        
                        if (rsServicio.next()) {
                        int idServicio = rsServicio.getInt("id");
                        jTextField_IDServicio.setText(String.valueOf(idServicio));
                        // Bloquea el campo jTextField_IDServicio 
                        jTextField_IDServicio.setEditable(false);
                        // Obtén el precio del servicio y actualiza el jTextFieldPrecio
                        double precio = rsServicio.getDouble("precio"); 
                        jTextField_precio.setText(String.valueOf(precio));
                        // Bloquea el campo jTextField_IDServicio 
                        jTextField_precio.setEditable(false);
                    } else {
                        JOptionPane.showMessageDialog(null, "Servicio no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                        
                    } catch (SQLException ex) {
                        System.out.println("Error al obtener el ID del servicio: " + ex.getMessage());
                        JOptionPane.showMessageDialog(null, "Error al obtener el ID del servicio: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        // Establecer iconos
        try {
            jLabel_appLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/logo.1.png")));
            //jLabel_close.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/x.png")));
        } catch (NullPointerException e) {
            System.out.println("Error al cargar imágenes: " + e.getMessage());
        }
        
        
        // Establecer bordes
        // Borde del logotipo del panel
        Border panelBorder = BorderFactory.createMatteBorder(0, 0, 2, 0, Color.lightGray);
        jPanel_logoANDname.setBorder(panelBorder);
        // panel container border
        Border containerBorder = BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(0,102,102));
        jPanel_container.setBorder(containerBorder);
        
        // Rellene la matriz de  menuLabels
        
        menuLabels[0] = jLabel_menuItem1;
        menuLabels[1] = jLabel_menuItem2;
        menuLabels[2] = jLabel_menuItem3;
        menuLabels[3] = jLabel_menuItem4;
        menuLabels[4] = jLabel_menuItem5;
        menuLabels[5] = jLabel_menuItem6;
        menuLabels[6] = jLabel_menuItem7;
        menuLabels[7] = jLabel_menuItem8;
        menuLabels[8] = jLabel_menuItem9;
        
        // Llenar la matriz de paneles
        panels[0] = jPanel_dashboard;
        panels[1] = jPanel_pacientes;
        panels[2] = jPanel_citas;
        panels[3] = jPanel_productos;
        panels[4] = jPanel_ventas;
        panels[5] = jPanel_proveedores;
        panels[6] = jPanel_reportes;
        panels[7] = jPanel_usuarios;
        panels[8] = jPanel_ayuda;
        
        addActionToMenuLabels();
        
    }
    
    //----------------------             --------------------------//  
                // Método para establecer el rol del usuario
    //----------------------             --------------------------// 
    
    public void setUserRole(String role) {
        this.userRole = role;
        adjustUIForRole();
    }
    
   // Método para ajustar la interfaz de usuario según el rol del usuario
    private void adjustUIForRole() {
        if ("Recepcionista".equalsIgnoreCase(userRole)) {
            jLabel7.setText("Bienvenido Recepcionista");
            //jLabel_menuItem1.setVisible(false); // Dashboard
            //jLabel_menuItem2.setVisible(false); // Pacientes
            //jLabel_menuItem3.setVisible(false); //Citas
            //jLabel_menuItem4.setVisible(false); // Productos
            //jLabel_menuItem5.setVisible(false); // Ventas
            //jLabel_menuItem6.setVisible(false); // Proveedores
            jLabel_menuItem7.setVisible(false); // Reportes
            jLabel_menuItem8.setVisible(false); // Usuarios
            
        } else if ("Administrador".equalsIgnoreCase(userRole)) {
            jLabel7.setText("Bienvenido Administrador");
            jLabel_menuItem7.setVisible(false); // Reportes
        } else if ("Optometrista".equalsIgnoreCase(userRole)) {
            jLabel7.setText("Bienvenido Optometrista");
            //jLabel_menuItem2.setVisible(false); // Pacientes
            jLabel_menuItem4.setVisible(false); // Producto
            jLabel_menuItem5.setVisible(false); // Ventas
            jLabel_menuItem6.setVisible(false); // Proveedores
            jLabel_menuItem7.setVisible(false); // Reportes
            jLabel_menuItem8.setVisible(false); // Usuarios
        }
    }

     // Cree una función para establecer el color de fondo de la etiqueta
    public void setLabelBackround(JLabel label) {
        // Restablecer las etiquetas a su diseño predeterminado
        for (JLabel menuItem : menuLabels) {
            // Cambia el color de fondo de JLabel
            menuItem.setBackground(new Color(0,102,102));
            // change the jlabel Foreground color to blue
            menuItem.setForeground(Color.white); 
        }

        // Cambia el color de fondo de JLabel 
        label.setBackground(new Color(0,102,102));
        // Cambia el color del primer plano de JLabel
        label.setForeground(Color.yellow);
    }
    
    // Cree una función para mostrar el panel seleccionado
    public void showPanel(JPanel panel) {
        // Ocultar paneles
        for (JPanel pnl : panels) {
            pnl.setVisible(false);
        }

        // Y mostrar solo este panel
        panel.setVisible(true);
    }

    
    
    public void addActionToMenuLabels() {
        // Obtener etiquetas en el menú de jpanel
        Component[] components = jPanel_menu.getComponents();
        
        for (Component component : components) {
            if(component instanceof JLabel) {
                JLabel label = (JLabel) component;
                
                label.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        // Cambia el fondo de JLabel y el primer plano
                        setLabelBackround(label);
                        
                        // Muestra el panel seleccionado
                        switch (label.getText().trim()) {
                            case "Dashboard":
                                showPanel(jPanel_dashboard);
                                break;
                            case "Pacientes":
                                showPanel(jPanel_pacientes);
                                break;
                            case "Citas":
                                showPanel(jPanel_citas);
                                break;
                            case "Productos":
                                showPanel(jPanel_productos);
                                break;
                            case "Ventas":
                                showPanel(jPanel_ventas);
                                break;
                            case "Proveedores":
                                showPanel(jPanel_proveedores);
                                break;
                            case "Reportes":
                                showPanel(jPanel_reportes);
                                break;
                            case "Usuarios":
                                showPanel(jPanel_usuarios);
                                break;
                            case "Ayuda y Soporte":
                                showPanel(jPanel_ayuda);
                                break;
                        }
                    }

                     @Override
                    public void mousePressed(MouseEvent e) {}

                    @Override
                    public void mouseReleased(MouseEvent e) {}

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        // Establezca el borde en amarillo
                        label.setBorder(yellow_border);
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        // Restablecer al borde predeterminado
                        label.setBorder(default_border);
                    }
                });
            }
        }
    }

    // nombre del usuario
     public void setUser(String name){
        user.setText(name);
    }
     
     


        
//----------------------             --------------------------//  
                  // conexcion base de datos
//----------------------             --------------------------//        
      
      private Connection conectarDB() {
    try {
        String SUrl = "jdbc:mysql://localhost:3306/opticagestioncitas";
        String SUser = "root";
        String SPass = "";
        return DriverManager.getConnection(SUrl, SUser, SPass); // Corrección: Usar SUrl, SUser, SPass
    } catch (SQLException ex) {
        // Manejar la excepción
        System.err.println("Error al conectar a la base de datos: " + ex.getMessage());
        return null;
    }
}
     
      // Método para obtener el ID del paciente de la base de datos
      
        private int obtenerIdPaciente() {
            int idPaciente = 0; // Inicializa el ID del paciente a 0

            try (Connection conexion = conectarDB(); // Obtiene la conexión a la base de datos
                PreparedStatement stmt = conexion.prepareStatement(
                       "SELECT Id FROM Pacientes WHERE Nombre = ? AND Apellido = ? AND Correo = ?")) {
                // Establece los valores de los parámetros de la consulta
                stmt.setString(1, NP.getText()); // Nombre del paciente
                stmt.setString(2, AP.getText()); // Apellido del paciente
                stmt.setString(3, CE.getText()); // Correo electrónico del paciente

                ResultSet rs = stmt.executeQuery(); // Ejecuta la consulta
                if (rs.next()) { // Si se encuentra un resultado
                idPaciente = rs.getInt("Id"); // Obtiene el ID del paciente del resultado
                }
            } catch (SQLException ex) {
            // Maneja la excepción
            JOptionPane.showMessageDialog(Dashboard_Form.this, "Error al obtener el ID del paciente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }

            return idPaciente; // Devuelve el ID del paciente
      }

      
    //----------------------             --------------------------//  
    // Método para cargar los datos en la tabla jTable_EEAG
    //----------------------             --------------------------//
   private void cargarTablaEEAG() {
    try (Connection conexion = conectarDB();
         PreparedStatement stmt = conexion.prepareStatement(
                 "SELECT * FROM Pacientes")) {

        ResultSet rs = stmt.executeQuery();

        // Obtener el modelo de la tabla
        DefaultTableModel model = (DefaultTableModel) jTable_EEAG.getModel();
        // Limpiar el modelo de la tabla
        model.setRowCount(0);

        // Verificar si el ID del usuario es válido
        if (Login.variableGlobalIdUsuario == 0) {
            JOptionPane.showMessageDialog(this, "No se ha encontrado el ID del usuario", "Error", JOptionPane.ERROR_MESSAGE);
            return; // Sale de la función si el ID no es válido
        }

        // Obtener el nombre del usuario (fuera del bucle)
        String nombreUsuario = "";
        try (PreparedStatement stmtUsuario = conexion.prepareStatement(
                "SELECT nombre_usuario FROM Usuarios WHERE id = ?")) {
            stmtUsuario.setInt(1, (int) Login.variableGlobalIdUsuario);
            ResultSet rsUsuario = stmtUsuario.executeQuery();
            if (rsUsuario.next()) {
                nombreUsuario = rsUsuario.getString("nombre_usuario");
            }
        }

        // Agregar los datos al modelo de la tabla
        while (rs.next()) {
            int id = rs.getInt("Id"); // Obtiene el ID del paciente
            String nombre = rs.getString("Nombre");
            String apellido = rs.getString("Apellido");
            String correo = rs.getString("Correo");
            String telefono = rs.getString("Telefono");
            String direccion = rs.getString("Direccion");  // Consigue la dirección
            String sexo = rs.getString("Sexo");          // Consigue el sexo
            Date fechaNacimiento = rs.getDate("Fecha_Nacimiento"); // Consigue la fecha de nacimiento

            // Agregar la fila al modelo de la tabla
            // **Ajuste aquí:** Ahora la columna ID se define como Integer
            model.addRow(new Object[]{id, nombre, apellido, correo, telefono, direccion, sexo, fechaNacimiento, nombreUsuario}); // Add all columns
        }

    } catch (SQLException ex) {
        // Manejar la excepción
        JOptionPane.showMessageDialog(this, "Error al cargar pacientes: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
   
     //----------------------             --------------------------//  
    // FIN Método para cargar los datos en la tabla jTable_EEAG
    //----------------------             --------------------------//
        
    
    
    //-------------- calcular edad --------------------//
    
    // Función para calcular la edad
        private int calcularEdad(java.sql.Date fechaNacimiento) {
            Calendar fechaNacimientoCalendar = Calendar.getInstance();
            fechaNacimientoCalendar.setTime(fechaNacimiento);

            Calendar hoy = Calendar.getInstance();

            int edad = hoy.get(Calendar.YEAR) - fechaNacimientoCalendar.get(Calendar.YEAR);

            // Ajustar la edad si el cumpleaños aún no ha pasado este año
            if (hoy.get(Calendar.MONTH) < fechaNacimientoCalendar.get(Calendar.MONTH) ||
                (hoy.get(Calendar.MONTH) == fechaNacimientoCalendar.get(Calendar.MONTH) &&
                 hoy.get(Calendar.DAY_OF_MONTH) < fechaNacimientoCalendar.get(Calendar.DAY_OF_MONTH))) {
                edad--;
            }

            return edad;
        }
    //-------------- Fin calcular edad --------------------//
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel_container = new javax.swing.JPanel();
        jPanel_menu = new javax.swing.JPanel();
        jPanel_logoANDname = new javax.swing.JPanel();
        jLabel_appLogo = new javax.swing.JLabel();
        jLabel_menuItem1 = new javax.swing.JLabel();
        jLabel_menuItem3 = new javax.swing.JLabel();
        jLabel_menuItem2 = new javax.swing.JLabel();
        jLabel_menuItem4 = new javax.swing.JLabel();
        jLabel_menuItem5 = new javax.swing.JLabel();
        jLabel_menuItem6 = new javax.swing.JLabel();
        jLabel_menuItem7 = new javax.swing.JLabel();
        jLabel_menuItem8 = new javax.swing.JLabel();
        jLabel_menuItem9 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        user = new javax.swing.JLabel();
        LogoutBtn = new javax.swing.JButton();
        jPanel_dashboard = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jPanel_pacientes = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel_Con = new javax.swing.JPanel();
        jLabel_CE = new javax.swing.JLabel();
        jLabel_TL = new javax.swing.JLabel();
        jLabel_AP = new javax.swing.JLabel();
        CE = new javax.swing.JTextField();
        AP = new javax.swing.JTextField();
        TL = new javax.swing.JTextField();
        jButton_registrar = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        jLabel_NP = new javax.swing.JLabel();
        NP = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable_EEAG = new javax.swing.JTable();
        jLabel21 = new javax.swing.JLabel();
        jButton_editar = new javax.swing.JButton();
        jButton_eliminar = new javax.swing.JButton();
        jButton_guardar = new javax.swing.JButton();
        jButton_MostrarPacientes = new javax.swing.JButton();
        jLabel_Dir = new javax.swing.JLabel();
        DC = new javax.swing.JTextField();
        jLabel_s = new javax.swing.JLabel();
        jComboBox_Sexo = new javax.swing.JComboBox<>();
        jLabel_fecha = new javax.swing.JLabel();
        jLabel_us = new javax.swing.JLabel();
        jTextField_IdA = new javax.swing.JTextField();
        jDateChooser_Nacimiento = new com.toedter.calendar.JDateChooser();
        jLabel_Pac = new javax.swing.JLabel();
        jTextField_IdPa = new javax.swing.JTextField();
        jPanel_Con1 = new javax.swing.JPanel();
        jLabel_BC1 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        NOP = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable_BSC = new javax.swing.JTable();
        jSeparator2 = new javax.swing.JSeparator();
        jButton_buscar = new javax.swing.JButton();
        jButton_LimpiarBusqueda = new javax.swing.JButton();
        jPanel_citas = new javax.swing.JPanel();
        jTabbedPane5 = new javax.swing.JTabbedPane();
        jPanel_historias1 = new javax.swing.JPanel();
        jLabel_ID = new javax.swing.JLabel();
        ID = new javax.swing.JTextField();
        jButton_ID = new javax.swing.JButton();
        jLabel_Tele = new javax.swing.JLabel();
        Tele = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        jLabel_Nom = new javax.swing.JLabel();
        Nom = new javax.swing.JTextField();
        jLabel_Ema = new javax.swing.JLabel();
        Ema = new javax.swing.JTextField();
        jLabel_Ape = new javax.swing.JLabel();
        Ape = new javax.swing.JTextField();
        jLabel_sex = new javax.swing.JLabel();
        Sex = new javax.swing.JTextField();
        jLabel_eda = new javax.swing.JLabel();
        Edad = new javax.swing.JTextField();
        jLabel_Direc = new javax.swing.JLabel();
        Dire = new javax.swing.JTextField();
        jLabel_FNa = new javax.swing.JLabel();
        jLabel_H = new javax.swing.JLabel();
        Hora = new javax.swing.JTextField();
        jLabel_motivo = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTextArea_Obse = new javax.swing.JTextArea();
        jLabel_Id1 = new javax.swing.JLabel();
        ID_user = new javax.swing.JTextField();
        jLabel_Rol = new javax.swing.JLabel();
        jLabel_MPago = new javax.swing.JLabel();
        jComboBox_pago = new javax.swing.JComboBox<>();
        jButton_Rcita = new javax.swing.JButton();
        jLabel_MPago1 = new javax.swing.JLabel();
        jCombo_Servicio = new javax.swing.JComboBox<>();
        jTextField_IDServicio = new javax.swing.JTextField();
        jLabel_IDservicio = new javax.swing.JLabel();
        jDateChooser_Cita = new com.toedter.calendar.JDateChooser();
        FN = new javax.swing.JTextField();
        jLabel_IDservicio1 = new javax.swing.JLabel();
        jTextField_precio = new javax.swing.JTextField();
        jPanel_Citas1 = new javax.swing.JPanel();
        jInternalFrame1 = new javax.swing.JInternalFrame();
        jLabel28 = new javax.swing.JLabel();
        jLabel_Id_1 = new javax.swing.JLabel();
        jTextField_Id11 = new javax.swing.JTextField();
        jButton_Id_11 = new javax.swing.JButton();
        jLabel_Tele1 = new javax.swing.JLabel();
        jTextField_Tf = new javax.swing.JTextField();
        jLabel_Nom1 = new javax.swing.JLabel();
        jTextField_Nom1 = new javax.swing.JTextField();
        jLabel_Ema1 = new javax.swing.JLabel();
        jTextField_em = new javax.swing.JTextField();
        jLabel_Ape1 = new javax.swing.JLabel();
        jTextField_Ape1 = new javax.swing.JTextField();
        jLabel_sex1 = new javax.swing.JLabel();
        jTextField_S = new javax.swing.JTextField();
        jLabel_eda1 = new javax.swing.JLabel();
        jTextField_E = new javax.swing.JTextField();
        jLabel_Direc1 = new javax.swing.JLabel();
        jTextField_Dire1 = new javax.swing.JTextField();
        jLabel_FNa1 = new javax.swing.JLabel();
        jTextField_FN1 = new javax.swing.JTextField();
        jLabel_H1 = new javax.swing.JLabel();
        jTextField_Hora1 = new javax.swing.JTextField();
        jLabel_motivo12 = new javax.swing.JLabel();
        jScrollPane14 = new javax.swing.JScrollPane();
        jTextArea_Obse12 = new javax.swing.JTextArea();
        jLabel_Fcita = new javax.swing.JLabel();
        jTextField_RC = new javax.swing.JTextField();
        jLabel_Direc2 = new javax.swing.JLabel();
        jLabel_Direc3 = new javax.swing.JLabel();
        jScrollPane15 = new javax.swing.JScrollPane();
        jTextArea_Trata13 = new javax.swing.JTextArea();
        jScrollPane16 = new javax.swing.JScrollPane();
        jTextArea_Diag14 = new javax.swing.JTextArea();
        jButton_RHC = new javax.swing.JButton();
        jInternalFrame2 = new javax.swing.JInternalFrame();
        jLabel37 = new javax.swing.JLabel();
        jButton_BusHistoria = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTable_Historias = new javax.swing.JTable();
        jPanel_productos = new javax.swing.JPanel();
        jTabbedPane6 = new javax.swing.JTabbedPane();
        jPanel_RGP = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        jTextField_codigo = new javax.swing.JTextField();
        jLabel_MP = new javax.swing.JLabel();
        jLabel_colorp = new javax.swing.JLabel();
        jTextField_marca = new javax.swing.JTextField();
        jTextField_producto = new javax.swing.JTextField();
        jTextField_cantidad = new javax.swing.JTextField();
        jLabel_produc = new javax.swing.JLabel();
        jTextField_color = new javax.swing.JTextField();
        jButton_RegisProducto = new javax.swing.JButton();
        jLabel_Prec = new javax.swing.JLabel();
        jTextField_venta = new javax.swing.JTextField();
        jLabel_PrecioV = new javax.swing.JLabel();
        jTextField_compra = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable_Productos = new javax.swing.JTable();
        jButton_edit = new javax.swing.JButton();
        jButton_mostrar = new javax.swing.JButton();
        jButton_guar = new javax.swing.JButton();
        jButton_elimi = new javax.swing.JButton();
        jLabel_fregis = new javax.swing.JLabel();
        jTextField_registro = new javax.swing.JTextField();
        jLabel_ca = new javax.swing.JLabel();
        jLabel_cd1 = new javax.swing.JLabel();
        jLabel_cd2 = new javax.swing.JLabel();
        jTextField_idProduc = new javax.swing.JTextField();
        jPanel_BSCP = new javax.swing.JPanel();
        jLabel38 = new javax.swing.JLabel();
        jLabel_cd3 = new javax.swing.JLabel();
        jTextField_Buscodigo1 = new javax.swing.JTextField();
        jButton_buscodigo1 = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable_buscarpro = new javax.swing.JTable();
        jPanel_GI = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        jButton_Gmostrar = new javax.swing.JButton();
        jScrollPane9 = new javax.swing.JScrollPane();
        jTable_MostrarProductos = new javax.swing.JTable();
        jPanel_ventas = new javax.swing.JPanel();
        jTabbedPane7 = new javax.swing.JTabbedPane();
        jPanel_VE = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        jPanel_LV = new javax.swing.JPanel();
        jPanel_BCPRO = new javax.swing.JPanel();
        jPanel_proveedores = new javax.swing.JPanel();
        jTabbedPane8 = new javax.swing.JTabbedPane();
        jPanel_RPRO = new javax.swing.JPanel();
        jLabel39 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jTextField_DirProveedor = new javax.swing.JTextField();
        jTextField_TelProveedor = new javax.swing.JTextField();
        jTextField_EmailProveedor = new javax.swing.JTextField();
        jTextField_RazonSocial = new javax.swing.JTextField();
        jTextField_NomProveedor = new javax.swing.JTextField();
        jTextField_FRegistro = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jTextField_RUC = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jTextField_Contacto = new javax.swing.JTextField();
        jButton_GuardProveedor = new javax.swing.JButton();
        jPanel_BCPROVE = new javax.swing.JPanel();
        jLabel43 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTable_proveedor = new javax.swing.JTable();
        jTextField_IDProveedor = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jButton_Proveedor = new javax.swing.JButton();
        jPanel_reportes = new javax.swing.JPanel();
        jTabbedPane9 = new javax.swing.JTabbedPane();
        jPanel_GRV = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        jPanel_GRVEN = new javax.swing.JPanel();
        jLabel40 = new javax.swing.JLabel();
        jPanel_GRI = new javax.swing.JPanel();
        jLabel41 = new javax.swing.JLabel();
        jPanel_usuarios = new javax.swing.JPanel();
        jTabbedPane10 = new javax.swing.JTabbedPane();
        jPanel_RNUS = new javax.swing.JPanel();
        jLabel42 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        fname1 = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        emailAddress1 = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        pass2 = new javax.swing.JPasswordField();
        jLabel_Rol1 = new javax.swing.JLabel();
        Roles1 = new javax.swing.JComboBox<>();
        SignUpBtn2 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel22 = new javax.swing.JLabel();
        jButton_MostrarUsuarios = new javax.swing.JButton();
        jButton_editar1 = new javax.swing.JButton();
        jButton_guardar1 = new javax.swing.JButton();
        jButton_eliminar1 = new javax.swing.JButton();
        jPanel_ayuda = new javax.swing.JPanel();
        jTabbedPane11 = new javax.swing.JTabbedPane();
        jPanel_CPS = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("DASHBOARD");

        jPanel_container.setBackground(new java.awt.Color(255, 255, 255));
        jPanel_container.setPreferredSize(new java.awt.Dimension(1132, 665));

        jPanel_menu.setBackground(new java.awt.Color(0, 102, 102));

        jPanel_logoANDname.setBackground(new java.awt.Color(0, 102, 102));

        jLabel_appLogo.setBackground(new java.awt.Color(0, 102, 102));
        jLabel_appLogo.setOpaque(true);

        javax.swing.GroupLayout jPanel_logoANDnameLayout = new javax.swing.GroupLayout(jPanel_logoANDname);
        jPanel_logoANDname.setLayout(jPanel_logoANDnameLayout);
        jPanel_logoANDnameLayout.setHorizontalGroup(
            jPanel_logoANDnameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_logoANDnameLayout.createSequentialGroup()
                .addContainerGap(53, Short.MAX_VALUE)
                .addComponent(jLabel_appLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );
        jPanel_logoANDnameLayout.setVerticalGroup(
            jPanel_logoANDnameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_logoANDnameLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel_appLogo, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel_menuItem1.setBackground(new java.awt.Color(0, 102, 102));
        jLabel_menuItem1.setFont(new java.awt.Font("Sitka Text", 3, 18)); // NOI18N
        jLabel_menuItem1.setForeground(new java.awt.Color(255, 255, 0));
        jLabel_menuItem1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_menuItem1.setText("  Dashboard");
        jLabel_menuItem1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jLabel_menuItem1.setOpaque(true);

        jLabel_menuItem3.setBackground(new java.awt.Color(0, 102, 102));
        jLabel_menuItem3.setFont(new java.awt.Font("Sitka Text", 3, 18)); // NOI18N
        jLabel_menuItem3.setForeground(new java.awt.Color(255, 255, 0));
        jLabel_menuItem3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_menuItem3.setText("Citas");
        jLabel_menuItem3.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jLabel_menuItem3.setOpaque(true);

        jLabel_menuItem2.setBackground(new java.awt.Color(0, 102, 102));
        jLabel_menuItem2.setFont(new java.awt.Font("Sitka Text", 3, 18)); // NOI18N
        jLabel_menuItem2.setForeground(new java.awt.Color(255, 255, 0));
        jLabel_menuItem2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_menuItem2.setText("Pacientes");
        jLabel_menuItem2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jLabel_menuItem2.setOpaque(true);

        jLabel_menuItem4.setBackground(new java.awt.Color(0, 102, 102));
        jLabel_menuItem4.setFont(new java.awt.Font("Sitka Text", 3, 18)); // NOI18N
        jLabel_menuItem4.setForeground(new java.awt.Color(255, 255, 0));
        jLabel_menuItem4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_menuItem4.setText("Productos");
        jLabel_menuItem4.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jLabel_menuItem4.setOpaque(true);

        jLabel_menuItem5.setBackground(new java.awt.Color(0, 102, 102));
        jLabel_menuItem5.setFont(new java.awt.Font("Sitka Text", 3, 18)); // NOI18N
        jLabel_menuItem5.setForeground(new java.awt.Color(255, 255, 0));
        jLabel_menuItem5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_menuItem5.setText("Ventas");
        jLabel_menuItem5.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jLabel_menuItem5.setOpaque(true);

        jLabel_menuItem6.setBackground(new java.awt.Color(0, 102, 102));
        jLabel_menuItem6.setFont(new java.awt.Font("Sitka Text", 3, 18)); // NOI18N
        jLabel_menuItem6.setForeground(new java.awt.Color(255, 255, 0));
        jLabel_menuItem6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_menuItem6.setText("Proveedores");
        jLabel_menuItem6.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jLabel_menuItem6.setOpaque(true);

        jLabel_menuItem7.setBackground(new java.awt.Color(0, 102, 102));
        jLabel_menuItem7.setFont(new java.awt.Font("Sitka Text", 3, 18)); // NOI18N
        jLabel_menuItem7.setForeground(new java.awt.Color(255, 255, 0));
        jLabel_menuItem7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_menuItem7.setText("  Reportes");
        jLabel_menuItem7.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jLabel_menuItem7.setOpaque(true);

        jLabel_menuItem8.setBackground(new java.awt.Color(0, 102, 102));
        jLabel_menuItem8.setFont(new java.awt.Font("Sitka Text", 3, 18)); // NOI18N
        jLabel_menuItem8.setForeground(new java.awt.Color(255, 255, 0));
        jLabel_menuItem8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_menuItem8.setText("Usuarios");
        jLabel_menuItem8.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jLabel_menuItem8.setOpaque(true);

        jLabel_menuItem9.setBackground(new java.awt.Color(0, 102, 102));
        jLabel_menuItem9.setFont(new java.awt.Font("Sitka Text", 3, 18)); // NOI18N
        jLabel_menuItem9.setForeground(new java.awt.Color(255, 255, 0));
        jLabel_menuItem9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_menuItem9.setText("Ayuda y Soporte");
        jLabel_menuItem9.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jLabel_menuItem9.setOpaque(true);

        javax.swing.GroupLayout jPanel_menuLayout = new javax.swing.GroupLayout(jPanel_menu);
        jPanel_menu.setLayout(jPanel_menuLayout);
        jPanel_menuLayout.setHorizontalGroup(
            jPanel_menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel_logoANDname, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel_menuItem1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel_menuItem4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel_menuItem2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel_menuItem3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel_menuItem5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel_menuItem6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel_menuItem7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel_menuItem8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel_menuItem9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel_menuLayout.setVerticalGroup(
            jPanel_menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_menuLayout.createSequentialGroup()
                .addComponent(jPanel_logoANDname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addComponent(jLabel_menuItem1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel_menuItem2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel_menuItem3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel_menuItem4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel_menuItem5, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel_menuItem6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel_menuItem7, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel_menuItem8, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel_menuItem9, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 67, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(0, 102, 102));

        jLabel7.setBackground(new java.awt.Color(255, 255, 51));
        jLabel7.setFont(new java.awt.Font("Sitka Text", 3, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 51));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Bienvenido  al sistema");

        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/user.png"))); // NOI18N

        user.setFont(new java.awt.Font("Sitka Text", 3, 14)); // NOI18N
        user.setForeground(new java.awt.Color(255, 255, 51));
        user.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        user.setText("John");

        LogoutBtn.setText("Salir");
        LogoutBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LogoutBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addGap(110, 110, 110)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(71, 71, 71))
                    .addComponent(user, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LogoutBtn)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(23, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(LogoutBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(25, 25, 25))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(user)
                        .addContainerGap())))
        );

        jPanel_dashboard.setBackground(new java.awt.Color(255, 255, 255));

        jPanel3.setBackground(new java.awt.Color(220, 220, 220));

        jLabel9.setBackground(new java.awt.Color(0, 153, 153));
        jLabel9.setFont(new java.awt.Font("Sitka Text", 3, 18)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Dashboard");
        jLabel9.setOpaque(true);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(105, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(220, 220, 220));

        jLabel10.setBackground(new java.awt.Color(0, 153, 153));
        jLabel10.setFont(new java.awt.Font("Sitka Text", 3, 18)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Usuarios");
        jLabel10.setOpaque(true);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(105, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(220, 220, 220));

        jLabel11.setBackground(new java.awt.Color(0, 153, 153));
        jLabel11.setFont(new java.awt.Font("Sitka Text", 3, 18)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Citas");
        jLabel11.setOpaque(true);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(105, Short.MAX_VALUE))
        );

        jPanel6.setBackground(new java.awt.Color(220, 220, 220));

        jLabel12.setBackground(new java.awt.Color(0, 153, 153));
        jLabel12.setFont(new java.awt.Font("Sitka Text", 3, 18)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("Ajustes");
        jLabel12.setOpaque(true);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(105, Short.MAX_VALUE))
        );

        jPanel7.setBackground(new java.awt.Color(220, 220, 220));

        jLabel13.setBackground(new java.awt.Color(0, 153, 153));
        jLabel13.setFont(new java.awt.Font("Sitka Text", 3, 18)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Contactos");
        jLabel13.setOpaque(true);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(105, Short.MAX_VALUE))
        );

        jPanel8.setBackground(new java.awt.Color(220, 220, 220));

        jLabel14.setBackground(new java.awt.Color(0, 153, 153));
        jLabel14.setFont(new java.awt.Font("Sitka Text", 3, 18)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("Calendario");
        jLabel14.setOpaque(true);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(105, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel_dashboardLayout = new javax.swing.GroupLayout(jPanel_dashboard);
        jPanel_dashboard.setLayout(jPanel_dashboardLayout);
        jPanel_dashboardLayout.setHorizontalGroup(
            jPanel_dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_dashboardLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel_dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(30, 30, 30)
                .addGroup(jPanel_dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                .addGroup(jPanel_dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(25, 25, 25))
        );
        jPanel_dashboardLayout.setVerticalGroup(
            jPanel_dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_dashboardLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel_dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(79, 79, 79)
                .addGroup(jPanel_dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(64, 64, 64))
        );

        jPanel_pacientes.setBackground(new java.awt.Color(255, 255, 255));

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel_Con.setBackground(new java.awt.Color(255, 255, 255));

        jLabel_CE.setFont(new java.awt.Font("Sitka Text", 3, 14)); // NOI18N
        jLabel_CE.setText("C. Electronico :");

        jLabel_TL.setFont(new java.awt.Font("Sitka Text", 3, 14)); // NOI18N
        jLabel_TL.setText("Telefono :");

        jLabel_AP.setFont(new java.awt.Font("Sitka Text", 3, 14)); // NOI18N
        jLabel_AP.setText("Apellidos :");

        CE.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        CE.setForeground(new java.awt.Color(102, 102, 102));

        AP.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        AP.setForeground(new java.awt.Color(102, 102, 102));

        TL.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        TL.setForeground(new java.awt.Color(102, 102, 102));

        jButton_registrar.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        jButton_registrar.setText("REGISTRAR");
        jButton_registrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_registrarActionPerformed(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("Sitka Text", 3, 18)); // NOI18N
        jLabel20.setText("REGISTRO DE PACIENTES");

        jLabel_NP.setFont(new java.awt.Font("Sitka Text", 3, 14)); // NOI18N
        jLabel_NP.setText("Nombres :");

        NP.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        NP.setForeground(new java.awt.Color(102, 102, 102));

        jTable_EEAG.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID_Paciente", "Nombre", "Apellido", "Correo", "Telefono", "Dirección", "Sexo", "F/ Nacimiento", "Registro Paciente"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable_EEAG.setToolTipText("");
        jTable_EEAG.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_NEXT_COLUMN);
        jTable_EEAG.setColumnSelectionAllowed(true);
        jScrollPane3.setViewportView(jTable_EEAG);
        jTable_EEAG.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        if (jTable_EEAG.getColumnModel().getColumnCount() > 0) {
            jTable_EEAG.getColumnModel().getColumn(0).setResizable(false);
            jTable_EEAG.getColumnModel().getColumn(1).setResizable(false);
            jTable_EEAG.getColumnModel().getColumn(2).setResizable(false);
            jTable_EEAG.getColumnModel().getColumn(3).setResizable(false);
            jTable_EEAG.getColumnModel().getColumn(4).setResizable(false);
            jTable_EEAG.getColumnModel().getColumn(5).setResizable(false);
            jTable_EEAG.getColumnModel().getColumn(6).setResizable(false);
            jTable_EEAG.getColumnModel().getColumn(7).setResizable(false);
            jTable_EEAG.getColumnModel().getColumn(8).setResizable(false);
        }

        jLabel21.setFont(new java.awt.Font("Sitka Text", 3, 18)); // NOI18N
        jLabel21.setText("LISTA DE PACIENTES ");

        jButton_editar.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        jButton_editar.setText("EDITAR");
        jButton_editar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_editarActionPerformed(evt);
            }
        });

        jButton_eliminar.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        jButton_eliminar.setText("ELIMINAR");
        jButton_eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_eliminarActionPerformed(evt);
            }
        });

        jButton_guardar.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        jButton_guardar.setText("GUARDAR");
        jButton_guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_guardarActionPerformed(evt);
            }
        });

        jButton_MostrarPacientes.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        jButton_MostrarPacientes.setText("Mostrar Pacientes");
        jButton_MostrarPacientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_MostrarPacientesActionPerformed(evt);
            }
        });

        jLabel_Dir.setFont(new java.awt.Font("Sitka Text", 3, 14)); // NOI18N
        jLabel_Dir.setText("Dirección :");

        DC.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        DC.setForeground(new java.awt.Color(102, 102, 102));

        jLabel_s.setFont(new java.awt.Font("Sitka Text", 3, 14)); // NOI18N
        jLabel_s.setText("Sexo :");

        jComboBox_Sexo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Hombre", "Mujer" }));

        jLabel_fecha.setFont(new java.awt.Font("Sitka Text", 3, 14)); // NOI18N
        jLabel_fecha.setText("F/Nacimiento :");

        jLabel_us.setFont(new java.awt.Font("Sitka Text", 3, 14)); // NOI18N
        jLabel_us.setText("Usuario ID :");

        jTextField_IdA.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_IdA.setForeground(new java.awt.Color(102, 102, 102));

        jLabel_Pac.setFont(new java.awt.Font("Sitka Text", 3, 14)); // NOI18N
        jLabel_Pac.setText("Paciente ID :");

        jTextField_IdPa.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_IdPa.setForeground(new java.awt.Color(102, 102, 102));
        jTextField_IdPa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_IdPaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel_ConLayout = new javax.swing.GroupLayout(jPanel_Con);
        jPanel_Con.setLayout(jPanel_ConLayout);
        jPanel_ConLayout.setHorizontalGroup(
            jPanel_ConLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_ConLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel_ConLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_ConLayout.createSequentialGroup()
                        .addGap(83, 83, 83)
                        .addComponent(NP, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addGroup(jPanel_ConLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel_CE)
                            .addComponent(jLabel_Dir, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel_ConLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(DC, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(CE, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 66, Short.MAX_VALUE)
                        .addComponent(jButton_registrar)
                        .addGap(61, 61, 61))
                    .addGroup(jPanel_ConLayout.createSequentialGroup()
                        .addGroup(jPanel_ConLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel_ConLayout.createSequentialGroup()
                                .addComponent(jLabel_fecha)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jDateChooser_Nacimiento, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel_ConLayout.createSequentialGroup()
                                .addGroup(jPanel_ConLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel_AP)
                                    .addComponent(jLabel_NP)
                                    .addComponent(jLabel_TL))
                                .addGap(4, 4, 4)
                                .addGroup(jPanel_ConLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(AP, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(TL, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(102, 102, 102)
                        .addGroup(jPanel_ConLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel_ConLayout.createSequentialGroup()
                                .addComponent(jLabel_s)
                                .addGap(18, 18, 18)
                                .addComponent(jComboBox_Sexo, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(147, 147, 147))
                            .addGroup(jPanel_ConLayout.createSequentialGroup()
                                .addComponent(jLabel_us)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField_IdA, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel_Pac)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField_IdPa, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_ConLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel20)
                .addGap(318, 318, 318))
            .addGroup(jPanel_ConLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_ConLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 853, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel_ConLayout.createSequentialGroup()
                        .addComponent(jLabel21)
                        .addGap(32, 32, 32)
                        .addComponent(jButton_MostrarPacientes)
                        .addGap(18, 18, 18)
                        .addComponent(jButton_editar, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton_guardar)
                        .addGap(18, 18, 18)
                        .addComponent(jButton_eliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel_ConLayout.setVerticalGroup(
            jPanel_ConLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_ConLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel20)
                .addGroup(jPanel_ConLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_ConLayout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addGroup(jPanel_ConLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel_NP)
                            .addComponent(NP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel_CE)
                            .addComponent(CE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(25, 25, 25))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_ConLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton_registrar)
                        .addGap(10, 10, 10)))
                .addGroup(jPanel_ConLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_AP)
                    .addComponent(AP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_Dir)
                    .addComponent(DC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel_ConLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_ConLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(jPanel_ConLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel_TL)
                            .addComponent(TL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_ConLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel_ConLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel_s)
                            .addComponent(jComboBox_Sexo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGroup(jPanel_ConLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_ConLayout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(jDateChooser_Nacimiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel_ConLayout.createSequentialGroup()
                        .addGroup(jPanel_ConLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel_ConLayout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addComponent(jLabel_fecha)
                                .addGap(78, 78, 78))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_ConLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel_ConLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel_ConLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel_us)
                                        .addComponent(jTextField_IdA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel_ConLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel_Pac)
                                        .addComponent(jTextField_IdPa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(77, 77, 77)))
                        .addGroup(jPanel_ConLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel21)
                            .addComponent(jButton_MostrarPacientes)
                            .addComponent(jButton_editar)
                            .addComponent(jButton_guardar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton_eliminar))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jTabbedPane1.addTab("REGISTRO DE PACIENTES", jPanel_Con);

        jPanel_Con1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel_BC1.setFont(new java.awt.Font("Sitka Text", 3, 18)); // NOI18N
        jLabel_BC1.setText("BUSCAR PACIENTE");

        jLabel15.setFont(new java.awt.Font("Sitka Text", 3, 14)); // NOI18N
        jLabel15.setText("Nombre del Paciente :");

        NOP.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        NOP.setForeground(new java.awt.Color(102, 102, 102));

        jTable_BSC.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID_Paciente", "Nombre", "Apellido", "Correo", "Telefono", "Dirección", "Sexo", "F/Nacimiento", "Registro Paciente"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable_BSC.setToolTipText("");
        jTable_BSC.setColumnSelectionAllowed(true);
        jScrollPane2.setViewportView(jTable_BSC);
        jTable_BSC.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        if (jTable_BSC.getColumnModel().getColumnCount() > 0) {
            jTable_BSC.getColumnModel().getColumn(0).setResizable(false);
            jTable_BSC.getColumnModel().getColumn(1).setResizable(false);
            jTable_BSC.getColumnModel().getColumn(2).setResizable(false);
            jTable_BSC.getColumnModel().getColumn(3).setResizable(false);
            jTable_BSC.getColumnModel().getColumn(4).setResizable(false);
            jTable_BSC.getColumnModel().getColumn(5).setResizable(false);
            jTable_BSC.getColumnModel().getColumn(6).setResizable(false);
            jTable_BSC.getColumnModel().getColumn(7).setResizable(false);
            jTable_BSC.getColumnModel().getColumn(8).setResizable(false);
        }

        jButton_buscar.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jButton_buscar.setText("Buscar");
        jButton_buscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_buscarActionPerformed(evt);
            }
        });

        jButton_LimpiarBusqueda.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jButton_LimpiarBusqueda.setText("Limpiar");
        jButton_LimpiarBusqueda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_LimpiarBusquedaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel_Con1Layout = new javax.swing.GroupLayout(jPanel_Con1);
        jPanel_Con1.setLayout(jPanel_Con1Layout);
        jPanel_Con1Layout.setHorizontalGroup(
            jPanel_Con1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_Con1Layout.createSequentialGroup()
                .addGroup(jPanel_Con1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_Con1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSeparator2))
                    .addGroup(jPanel_Con1Layout.createSequentialGroup()
                        .addGroup(jPanel_Con1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel_Con1Layout.createSequentialGroup()
                                .addGap(340, 340, 340)
                                .addComponent(jLabel_BC1, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel_Con1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 856, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel_Con1Layout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addComponent(jLabel15)
                .addGap(18, 18, 18)
                .addComponent(NOP, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(103, 103, 103)
                .addComponent(jButton_buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                .addComponent(jButton_LimpiarBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
        );
        jPanel_Con1Layout.setVerticalGroup(
            jPanel_Con1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_Con1Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel_BC1)
                .addGap(42, 42, 42)
                .addGroup(jPanel_Con1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(NOP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_buscar)
                    .addComponent(jButton_LimpiarBusqueda))
                .addGap(35, 35, 35)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40))
        );

        jTabbedPane1.addTab("BUSCAR DE PACIENTES", jPanel_Con1);

        javax.swing.GroupLayout jPanel_pacientesLayout = new javax.swing.GroupLayout(jPanel_pacientes);
        jPanel_pacientes.setLayout(jPanel_pacientesLayout);
        jPanel_pacientesLayout.setHorizontalGroup(
            jPanel_pacientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_pacientesLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 897, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel_pacientesLayout.setVerticalGroup(
            jPanel_pacientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_pacientesLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 544, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel_citas.setBackground(new java.awt.Color(255, 255, 255));

        jTabbedPane5.setBackground(new java.awt.Color(255, 255, 255));

        jPanel_historias1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel_ID.setFont(new java.awt.Font("Sitka Text", 3, 14)); // NOI18N
        jLabel_ID.setText("ID Paciente :");

        ID.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        ID.setForeground(new java.awt.Color(102, 102, 102));

        jButton_ID.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jButton_ID.setText("Buscar ID");
        jButton_ID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_IDActionPerformed(evt);
            }
        });

        jLabel_Tele.setFont(new java.awt.Font("Sitka Text", 3, 14)); // NOI18N
        jLabel_Tele.setText("Telefono :");

        Tele.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Tele.setForeground(new java.awt.Color(102, 102, 102));
        Tele.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TeleActionPerformed(evt);
            }
        });

        jLabel36.setFont(new java.awt.Font("Sitka Text", 3, 18)); // NOI18N
        jLabel36.setText("REGISTRO DE CITAS");

        jLabel_Nom.setFont(new java.awt.Font("Sitka Text", 3, 14)); // NOI18N
        jLabel_Nom.setText("Nombre : ");

        Nom.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Nom.setForeground(new java.awt.Color(102, 102, 102));
        Nom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NomActionPerformed(evt);
            }
        });

        jLabel_Ema.setFont(new java.awt.Font("Sitka Text", 3, 14)); // NOI18N
        jLabel_Ema.setText("Email  :");

        Ema.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Ema.setForeground(new java.awt.Color(102, 102, 102));
        Ema.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EmaActionPerformed(evt);
            }
        });

        jLabel_Ape.setFont(new java.awt.Font("Sitka Text", 3, 14)); // NOI18N
        jLabel_Ape.setText(" Apellido : ");

        Ape.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Ape.setForeground(new java.awt.Color(102, 102, 102));
        Ape.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ApeActionPerformed(evt);
            }
        });

        jLabel_sex.setFont(new java.awt.Font("Sitka Text", 3, 14)); // NOI18N
        jLabel_sex.setText("Sexo :");

        Sex.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Sex.setForeground(new java.awt.Color(102, 102, 102));

        jLabel_eda.setFont(new java.awt.Font("Sitka Text", 3, 14)); // NOI18N
        jLabel_eda.setText("Edad :");

        Edad.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Edad.setForeground(new java.awt.Color(102, 102, 102));

        jLabel_Direc.setFont(new java.awt.Font("Sitka Text", 3, 14)); // NOI18N
        jLabel_Direc.setText("Dirección :");

        Dire.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Dire.setForeground(new java.awt.Color(102, 102, 102));
        Dire.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DireActionPerformed(evt);
            }
        });

        jLabel_FNa.setFont(new java.awt.Font("Sitka Text", 3, 14)); // NOI18N
        jLabel_FNa.setText("F/Nacimiento  :");

        jLabel_H.setFont(new java.awt.Font("Sitka Text", 3, 14)); // NOI18N
        jLabel_H.setText("Hora de Cita :");

        Hora.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Hora.setForeground(new java.awt.Color(102, 102, 102));

        jLabel_motivo.setFont(new java.awt.Font("Sitka Text", 3, 14)); // NOI18N
        jLabel_motivo.setText("Observaciones :");

        jTextArea_Obse.setColumns(20);
        jTextArea_Obse.setRows(5);
        jScrollPane8.setViewportView(jTextArea_Obse);

        jLabel_Id1.setFont(new java.awt.Font("Sitka Text", 3, 14)); // NOI18N
        jLabel_Id1.setText("ID del Encargado del registro :");

        ID_user.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        ID_user.setForeground(new java.awt.Color(102, 102, 102));

        jLabel_Rol.setFont(new java.awt.Font("Sitka Text", 3, 14)); // NOI18N
        jLabel_Rol.setText("Registro la Cita :");

        jLabel_MPago.setFont(new java.awt.Font("Sitka Text", 3, 14)); // NOI18N
        jLabel_MPago.setText("METODO DE  PAGO :");

        jComboBox_pago.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Visa", "Plim", "Yape", "Paypal" }));

        jButton_Rcita.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        jButton_Rcita.setText("REGISTRAR CITA");
        jButton_Rcita.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_RcitaActionPerformed(evt);
            }
        });

        jLabel_MPago1.setFont(new java.awt.Font("Sitka Text", 3, 14)); // NOI18N
        jLabel_MPago1.setText("Servicio :");

        jCombo_Servicio.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Examen de la Vista", "Lentes de Contacto", "Armazón de Anteojos", "Lentes Oftálmicos", "Limpieza y Ajuste de Anteojos" }));
        jCombo_Servicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCombo_ServicioActionPerformed(evt);
            }
        });

        jTextField_IDServicio.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_IDServicio.setForeground(new java.awt.Color(102, 102, 102));

        jLabel_IDservicio.setFont(new java.awt.Font("Sitka Text", 3, 14)); // NOI18N
        jLabel_IDservicio.setText("ID Servicio:");

        FN.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        FN.setForeground(new java.awt.Color(102, 102, 102));

        jLabel_IDservicio1.setFont(new java.awt.Font("Sitka Text", 3, 14)); // NOI18N
        jLabel_IDservicio1.setText("Precio:");

        jTextField_precio.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_precio.setForeground(new java.awt.Color(102, 102, 102));
        jTextField_precio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_precioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel_historias1Layout = new javax.swing.GroupLayout(jPanel_historias1);
        jPanel_historias1.setLayout(jPanel_historias1Layout);
        jPanel_historias1Layout.setHorizontalGroup(
            jPanel_historias1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_historias1Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel_historias1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_historias1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE)
                        .addComponent(jLabel_motivo)
                        .addGroup(jPanel_historias1Layout.createSequentialGroup()
                            .addGroup(jPanel_historias1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel_Ape)
                                .addComponent(jLabel_Direc)
                                .addComponent(jLabel_Nom))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel_historias1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(Dire)
                                .addComponent(Ape)
                                .addComponent(Nom))))
                    .addGroup(jPanel_historias1Layout.createSequentialGroup()
                        .addComponent(jLabel_ID, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ID, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton_ID)))
                .addGap(125, 125, 125)
                .addGroup(jPanel_historias1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_historias1Layout.createSequentialGroup()
                        .addComponent(jLabel_Id1, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ID_user, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel_historias1Layout.createSequentialGroup()
                        .addGroup(jPanel_historias1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel_historias1Layout.createSequentialGroup()
                                .addGroup(jPanel_historias1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel_H, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel_FNa))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel_historias1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Hora)
                                    .addComponent(FN)))
                            .addGroup(jPanel_historias1Layout.createSequentialGroup()
                                .addGroup(jPanel_historias1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel_sex)
                                    .addGroup(jPanel_historias1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel_Tele)
                                        .addComponent(jLabel_Ema, javax.swing.GroupLayout.Alignment.TRAILING)))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel_historias1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel_historias1Layout.createSequentialGroup()
                                        .addComponent(Sex, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel_eda)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(Edad, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(Ema)
                                    .addComponent(Tele))
                                .addGap(0, 11, Short.MAX_VALUE))
                            .addGroup(jPanel_historias1Layout.createSequentialGroup()
                                .addComponent(jLabel_Rol)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jDateChooser_Cita, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(111, 111, 111))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_historias1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel_historias1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_historias1Layout.createSequentialGroup()
                        .addComponent(jButton_Rcita)
                        .addGap(40, 40, 40))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_historias1Layout.createSequentialGroup()
                        .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(342, 342, 342))))
            .addGroup(jPanel_historias1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel_MPago, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox_pago, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel_MPago1, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCombo_Servicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel_IDservicio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField_IDServicio, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel_IDservicio1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField_precio, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel_historias1Layout.setVerticalGroup(
            jPanel_historias1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_historias1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel36)
                .addGap(25, 25, 25)
                .addGroup(jPanel_historias1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_ID)
                    .addComponent(ID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_ID)
                    .addComponent(jLabel_Tele)
                    .addComponent(Tele, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(jPanel_historias1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_historias1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel_Nom)
                        .addComponent(Nom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel_historias1Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(jPanel_historias1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel_Ema)
                            .addComponent(Ema, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(20, 20, 20)
                .addGroup(jPanel_historias1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_historias1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel_Ape)
                        .addComponent(Ape, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel_historias1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(Sex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel_sex)
                        .addComponent(Edad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel_eda)))
                .addGap(20, 20, 20)
                .addGroup(jPanel_historias1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_Direc)
                    .addComponent(Dire, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_FNa)
                    .addComponent(FN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel_historias1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel_historias1Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(jPanel_historias1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel_H)
                            .addComponent(Hora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton_Rcita)
                        .addGap(23, 23, 23))
                    .addGroup(jPanel_historias1Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(jLabel_motivo)
                        .addGap(20, 20, 20)
                        .addGroup(jPanel_historias1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel_historias1Layout.createSequentialGroup()
                                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(36, 36, 36))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_historias1Layout.createSequentialGroup()
                                .addGroup(jPanel_historias1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel_Rol)
                                    .addComponent(jDateChooser_Cita, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel_historias1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel_Id1)
                                    .addComponent(ID_user, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(38, 38, 38)))
                        .addGroup(jPanel_historias1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel_historias1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jTextField_precio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel_IDservicio1))
                            .addGroup(jPanel_historias1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel_MPago)
                                .addComponent(jComboBox_pago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel_MPago1)
                                .addComponent(jCombo_Servicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextField_IDServicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel_IDservicio)))
                        .addContainerGap(79, Short.MAX_VALUE))))
        );

        jTabbedPane5.addTab("CITAS", jPanel_historias1);

        jPanel_Citas1.setBackground(new java.awt.Color(255, 255, 255));

        jInternalFrame1.setBackground(new java.awt.Color(255, 255, 255));
        jInternalFrame1.setVisible(true);

        jLabel28.setFont(new java.awt.Font("Sitka Text", 3, 18)); // NOI18N
        jLabel28.setText("Registro Historias Clinicas");

        jLabel_Id_1.setFont(new java.awt.Font("Sitka Text", 3, 14)); // NOI18N
        jLabel_Id_1.setText("ID Paciente :");

        jTextField_Id11.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_Id11.setForeground(new java.awt.Color(102, 102, 102));

        jButton_Id_11.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jButton_Id_11.setText("Buscar ID");
        jButton_Id_11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_Id_11ActionPerformed(evt);
            }
        });

        jLabel_Tele1.setFont(new java.awt.Font("Sitka Text", 3, 14)); // NOI18N
        jLabel_Tele1.setText("Telefono :");

        jTextField_Tf.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_Tf.setForeground(new java.awt.Color(102, 102, 102));
        jTextField_Tf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_TfActionPerformed(evt);
            }
        });

        jLabel_Nom1.setFont(new java.awt.Font("Sitka Text", 3, 14)); // NOI18N
        jLabel_Nom1.setText("Nombre : ");

        jTextField_Nom1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_Nom1.setForeground(new java.awt.Color(102, 102, 102));
        jTextField_Nom1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_Nom1ActionPerformed(evt);
            }
        });

        jLabel_Ema1.setFont(new java.awt.Font("Sitka Text", 3, 14)); // NOI18N
        jLabel_Ema1.setText("Email  :");

        jTextField_em.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_em.setForeground(new java.awt.Color(102, 102, 102));
        jTextField_em.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_emActionPerformed(evt);
            }
        });

        jLabel_Ape1.setFont(new java.awt.Font("Sitka Text", 3, 14)); // NOI18N
        jLabel_Ape1.setText(" Apellido : ");

        jTextField_Ape1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_Ape1.setForeground(new java.awt.Color(102, 102, 102));
        jTextField_Ape1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_Ape1ActionPerformed(evt);
            }
        });

        jLabel_sex1.setFont(new java.awt.Font("Sitka Text", 3, 14)); // NOI18N
        jLabel_sex1.setText("Sexo :");

        jTextField_S.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_S.setForeground(new java.awt.Color(102, 102, 102));

        jLabel_eda1.setFont(new java.awt.Font("Sitka Text", 3, 14)); // NOI18N
        jLabel_eda1.setText("Edad :");

        jTextField_E.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_E.setForeground(new java.awt.Color(102, 102, 102));

        jLabel_Direc1.setFont(new java.awt.Font("Sitka Text", 3, 14)); // NOI18N
        jLabel_Direc1.setText("Dirección :");

        jTextField_Dire1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_Dire1.setForeground(new java.awt.Color(102, 102, 102));
        jTextField_Dire1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_Dire1ActionPerformed(evt);
            }
        });

        jLabel_FNa1.setFont(new java.awt.Font("Sitka Text", 3, 14)); // NOI18N
        jLabel_FNa1.setText("F/Nacimiento  :");

        jTextField_FN1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_FN1.setForeground(new java.awt.Color(102, 102, 102));

        jLabel_H1.setFont(new java.awt.Font("Sitka Text", 3, 14)); // NOI18N
        jLabel_H1.setText("Hora de Cita :");

        jTextField_Hora1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_Hora1.setForeground(new java.awt.Color(102, 102, 102));

        jLabel_motivo12.setFont(new java.awt.Font("Sitka Text", 3, 14)); // NOI18N
        jLabel_motivo12.setText("Motivo de la consulta :");

        jTextArea_Obse12.setColumns(20);
        jTextArea_Obse12.setRows(5);
        jScrollPane14.setViewportView(jTextArea_Obse12);

        jLabel_Fcita.setFont(new java.awt.Font("Sitka Text", 3, 14)); // NOI18N
        jLabel_Fcita.setText("Fecha de Cita :");

        jTextField_RC.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_RC.setForeground(new java.awt.Color(102, 102, 102));
        jTextField_RC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_RCActionPerformed(evt);
            }
        });

        jLabel_Direc2.setFont(new java.awt.Font("Sitka Text", 3, 14)); // NOI18N
        jLabel_Direc2.setText("Diagnostico : ");

        jLabel_Direc3.setFont(new java.awt.Font("Sitka Text", 3, 14)); // NOI18N
        jLabel_Direc3.setText("Tratamiento :");

        jTextArea_Trata13.setColumns(20);
        jTextArea_Trata13.setRows(5);
        jScrollPane15.setViewportView(jTextArea_Trata13);

        jTextArea_Diag14.setColumns(20);
        jTextArea_Diag14.setRows(5);
        jScrollPane16.setViewportView(jTextArea_Diag14);

        jButton_RHC.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jButton_RHC.setText("Guardar Historias Clinica ");
        jButton_RHC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_RHCActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jInternalFrame1Layout = new javax.swing.GroupLayout(jInternalFrame1.getContentPane());
        jInternalFrame1.getContentPane().setLayout(jInternalFrame1Layout);
        jInternalFrame1Layout.setHorizontalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jInternalFrame1Layout.createSequentialGroup()
                .addGap(296, 296, 296)
                .addComponent(jLabel28)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jInternalFrame1Layout.createSequentialGroup()
                .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jInternalFrame1Layout.createSequentialGroup()
                        .addGap(64, 64, 64)
                        .addComponent(jLabel_Direc2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane16, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 116, Short.MAX_VALUE)
                        .addComponent(jLabel_Direc3)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jInternalFrame1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel_Fcita)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField_RC, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(76, 76, 76))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jInternalFrame1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton_RHC)
                .addGap(296, 296, 296))
            .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jInternalFrame1Layout.createSequentialGroup()
                    .addGap(66, 66, 66)
                    .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane14, javax.swing.GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE)
                            .addComponent(jLabel_motivo12)
                            .addGroup(jInternalFrame1Layout.createSequentialGroup()
                                .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel_Ape1)
                                    .addComponent(jLabel_Direc1)
                                    .addComponent(jLabel_Nom1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField_Dire1)
                                    .addComponent(jTextField_Ape1)
                                    .addComponent(jTextField_Nom1))))
                        .addGroup(jInternalFrame1Layout.createSequentialGroup()
                            .addComponent(jLabel_Id_1, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField_Id11, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jButton_Id_11)))
                    .addGap(125, 125, 125)
                    .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jInternalFrame1Layout.createSequentialGroup()
                            .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel_H1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel_FNa1))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jTextField_Hora1)
                                .addComponent(jTextField_FN1, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)))
                        .addGroup(jInternalFrame1Layout.createSequentialGroup()
                            .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel_sex1)
                                .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel_Tele1)
                                    .addComponent(jLabel_Ema1, javax.swing.GroupLayout.Alignment.TRAILING)))
                            .addGap(18, 18, 18)
                            .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jInternalFrame1Layout.createSequentialGroup()
                                    .addComponent(jTextField_S, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jLabel_eda1)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTextField_E, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jTextField_em)
                                .addComponent(jTextField_Tf))))
                    .addGap(77, 77, 77)))
        );
        jInternalFrame1Layout.setVerticalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jInternalFrame1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel28)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 260, Short.MAX_VALUE)
                .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField_RC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_Fcita))
                .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jInternalFrame1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel_Direc2)
                        .addGap(81, 81, 81))
                    .addGroup(jInternalFrame1Layout.createSequentialGroup()
                        .addGap(67, 67, 67)
                        .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane16, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jInternalFrame1Layout.createSequentialGroup()
                                .addComponent(jLabel_Direc3)
                                .addGap(30, 30, 30))
                            .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                        .addComponent(jButton_RHC)
                        .addContainerGap())))
            .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jInternalFrame1Layout.createSequentialGroup()
                    .addGap(58, 58, 58)
                    .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton_Id_11)
                        .addComponent(jTextField_Id11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel_Id_1)
                        .addComponent(jLabel_Tele1)
                        .addComponent(jTextField_Tf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(25, 25, 25)
                    .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel_Nom1)
                            .addComponent(jTextField_Nom1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jInternalFrame1Layout.createSequentialGroup()
                            .addGap(1, 1, 1)
                            .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel_Ema1)
                                .addComponent(jTextField_em, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGap(20, 20, 20)
                    .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel_Ape1)
                            .addComponent(jTextField_Ape1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField_S, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel_sex1)
                            .addComponent(jTextField_E, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel_eda1)))
                    .addGap(20, 20, 20)
                    .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel_Direc1)
                        .addComponent(jTextField_Dire1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel_FNa1)
                        .addComponent(jTextField_FN1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(25, 25, 25)
                    .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel_H1)
                            .addComponent(jTextField_Hora1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jInternalFrame1Layout.createSequentialGroup()
                            .addComponent(jLabel_motivo12)
                            .addGap(20, 20, 20)
                            .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap(151, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout jPanel_Citas1Layout = new javax.swing.GroupLayout(jPanel_Citas1);
        jPanel_Citas1.setLayout(jPanel_Citas1Layout);
        jPanel_Citas1Layout.setHorizontalGroup(
            jPanel_Citas1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jInternalFrame1)
        );
        jPanel_Citas1Layout.setVerticalGroup(
            jPanel_Citas1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jInternalFrame1)
        );

        jTabbedPane5.addTab("REGISTRO HISTORIAS CLINICAS", jPanel_Citas1);

        jInternalFrame2.setBackground(new java.awt.Color(255, 255, 255));
        jInternalFrame2.setVisible(true);

        jLabel37.setFont(new java.awt.Font("Sitka Text", 3, 18)); // NOI18N
        jLabel37.setText("Buscar Historias Clinicas");

        jButton_BusHistoria.setText("Buscar Historia");
        jButton_BusHistoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_BusHistoriaActionPerformed(evt);
            }
        });

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jLabel31.setText("Nombre del Paciente :");

        jTable_Historias.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Nombre", "Apellidos", "Direccion", "Correo", "Sexo", "Generar Historial"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane7.setViewportView(jTable_Historias);

        javax.swing.GroupLayout jInternalFrame2Layout = new javax.swing.GroupLayout(jInternalFrame2.getContentPane());
        jInternalFrame2.getContentPane().setLayout(jInternalFrame2Layout);
        jInternalFrame2Layout.setHorizontalGroup(
            jInternalFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jInternalFrame2Layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton_BusHistoria, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(425, 425, 425))
            .addGroup(jInternalFrame2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane7)
                .addContainerGap())
            .addGroup(jInternalFrame2Layout.createSequentialGroup()
                .addGap(315, 315, 315)
                .addComponent(jLabel37)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jInternalFrame2Layout.setVerticalGroup(
            jInternalFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jInternalFrame2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel37)
                .addGap(39, 39, 39)
                .addGroup(jInternalFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_BusHistoria)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel31))
                .addGap(45, 45, 45)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(98, Short.MAX_VALUE))
        );

        jTabbedPane5.addTab("BUSCAR HISTORIA CLINICA", jInternalFrame2);

        javax.swing.GroupLayout jPanel_citasLayout = new javax.swing.GroupLayout(jPanel_citas);
        jPanel_citas.setLayout(jPanel_citasLayout);
        jPanel_citasLayout.setHorizontalGroup(
            jPanel_citasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_citasLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTabbedPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 891, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel_citasLayout.setVerticalGroup(
            jPanel_citasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_citasLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTabbedPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 558, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );

        jPanel_productos.setBackground(new java.awt.Color(255, 255, 255));

        jTabbedPane6.setBackground(new java.awt.Color(255, 255, 255));

        jPanel_RGP.setBackground(new java.awt.Color(255, 255, 255));

        jLabel35.setFont(new java.awt.Font("Sitka Text", 3, 18)); // NOI18N
        jLabel35.setText("REGISTRO DE PRODUCTOS");

        jTextField_codigo.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_codigo.setForeground(new java.awt.Color(102, 102, 102));

        jLabel_MP.setFont(new java.awt.Font("Sitka Text", 3, 14)); // NOI18N
        jLabel_MP.setText("MARCA  PRODUCTO :");

        jLabel_colorp.setFont(new java.awt.Font("Sitka Text", 3, 14)); // NOI18N
        jLabel_colorp.setText("COLOR  PRODUCTO :");

        jTextField_marca.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_marca.setForeground(new java.awt.Color(102, 102, 102));

        jTextField_producto.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_producto.setForeground(new java.awt.Color(102, 102, 102));

        jTextField_cantidad.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_cantidad.setForeground(new java.awt.Color(102, 102, 102));

        jLabel_produc.setFont(new java.awt.Font("Sitka Text", 3, 14)); // NOI18N
        jLabel_produc.setText("NOMBRE PRODUCTO :");

        jTextField_color.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_color.setForeground(new java.awt.Color(102, 102, 102));

        jButton_RegisProducto.setText("Registro- Producto");
        jButton_RegisProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_RegisProductoActionPerformed(evt);
            }
        });

        jLabel_Prec.setFont(new java.awt.Font("Sitka Text", 3, 14)); // NOI18N
        jLabel_Prec.setText("PRECIO COMPRA :");

        jTextField_venta.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_venta.setForeground(new java.awt.Color(102, 102, 102));

        jLabel_PrecioV.setFont(new java.awt.Font("Sitka Text", 3, 14)); // NOI18N
        jLabel_PrecioV.setText("PRECIO VENTA :");

        jTextField_compra.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_compra.setForeground(new java.awt.Color(102, 102, 102));

        jTable_Productos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Codigo", "Nombre Producto", "Cantidad", "Marca", "Color Producto", "Fecha Registro", "Precio Compra", "Precio Venta"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTable_Productos.setColumnSelectionAllowed(true);
        jScrollPane1.setViewportView(jTable_Productos);
        jTable_Productos.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        if (jTable_Productos.getColumnModel().getColumnCount() > 0) {
            jTable_Productos.getColumnModel().getColumn(0).setPreferredWidth(50);
            jTable_Productos.getColumnModel().getColumn(1).setPreferredWidth(100);
            jTable_Productos.getColumnModel().getColumn(2).setPreferredWidth(150);
            jTable_Productos.getColumnModel().getColumn(3).setPreferredWidth(70);
            jTable_Productos.getColumnModel().getColumn(4).setPreferredWidth(80);
            jTable_Productos.getColumnModel().getColumn(5).setPreferredWidth(100);
            jTable_Productos.getColumnModel().getColumn(6).setPreferredWidth(120);
            jTable_Productos.getColumnModel().getColumn(7).setPreferredWidth(100);
            jTable_Productos.getColumnModel().getColumn(8).setPreferredWidth(100);
        }

        jButton_edit.setText("EDITAR");
        jButton_edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_editActionPerformed(evt);
            }
        });

        jButton_mostrar.setText("MOSTRAR");
        jButton_mostrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_mostrarActionPerformed(evt);
            }
        });

        jButton_guar.setText("GUARDAR");
        jButton_guar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_guarActionPerformed(evt);
            }
        });

        jButton_elimi.setText("ELIMINAR");
        jButton_elimi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_elimiActionPerformed(evt);
            }
        });

        jLabel_fregis.setFont(new java.awt.Font("Sitka Text", 3, 14)); // NOI18N
        jLabel_fregis.setText("F/ REGISTRO:");

        jTextField_registro.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_registro.setForeground(new java.awt.Color(102, 102, 102));

        jLabel_ca.setFont(new java.awt.Font("Sitka Text", 3, 14)); // NOI18N
        jLabel_ca.setText("CANTIDAD:");

        jLabel_cd1.setFont(new java.awt.Font("Sitka Text", 3, 14)); // NOI18N
        jLabel_cd1.setText("CODIGO  :");

        jLabel_cd2.setFont(new java.awt.Font("Sitka Text", 3, 14)); // NOI18N
        jLabel_cd2.setText("ID  :");

        jTextField_idProduc.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_idProduc.setForeground(new java.awt.Color(102, 102, 102));
        jTextField_idProduc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_idProducActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel_RGPLayout = new javax.swing.GroupLayout(jPanel_RGP);
        jPanel_RGP.setLayout(jPanel_RGPLayout);
        jPanel_RGPLayout.setHorizontalGroup(
            jPanel_RGPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_RGPLayout.createSequentialGroup()
                .addGroup(jPanel_RGPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_RGPLayout.createSequentialGroup()
                        .addGap(347, 347, 347)
                        .addComponent(jLabel35))
                    .addGroup(jPanel_RGPLayout.createSequentialGroup()
                        .addGroup(jPanel_RGPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel_RGPLayout.createSequentialGroup()
                                .addGap(38, 38, 38)
                                .addGroup(jPanel_RGPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel_MP)
                                    .addComponent(jLabel_colorp))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel_RGPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField_color, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField_marca, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel_RGPLayout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addComponent(jLabel_produc)
                                .addGap(18, 18, 18)
                                .addComponent(jTextField_producto, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(32, 32, 32)
                        .addGroup(jPanel_RGPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel_RGPLayout.createSequentialGroup()
                                .addComponent(jLabel_Prec, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField_compra))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_RGPLayout.createSequentialGroup()
                                .addComponent(jLabel_fregis)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField_registro))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_RGPLayout.createSequentialGroup()
                                .addComponent(jLabel_PrecioV)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                                .addComponent(jTextField_venta, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(46, 46, 46)
                        .addGroup(jPanel_RGPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel_RGPLayout.createSequentialGroup()
                                .addComponent(jLabel_cd1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jTextField_codigo, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel_RGPLayout.createSequentialGroup()
                                .addGroup(jPanel_RGPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel_cd2)
                                    .addComponent(jLabel_ca))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel_RGPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField_cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField_idProduc, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel_RGPLayout.createSequentialGroup()
                        .addGap(162, 162, 162)
                        .addComponent(jButton_mostrar, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(48, 48, 48)
                        .addComponent(jButton_edit, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(50, 50, 50)
                        .addComponent(jButton_guar, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39)
                        .addComponent(jButton_elimi, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel_RGPLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 843, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(24, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_RGPLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton_RegisProducto)
                .addGap(351, 351, 351))
        );
        jPanel_RGPLayout.setVerticalGroup(
            jPanel_RGPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_RGPLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel35)
                .addGap(32, 32, 32)
                .addGroup(jPanel_RGPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_produc)
                    .addComponent(jTextField_producto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_Prec)
                    .addComponent(jTextField_compra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_ca)
                    .addComponent(jTextField_cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(jPanel_RGPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_MP)
                    .addComponent(jTextField_marca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_PrecioV)
                    .addComponent(jTextField_venta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_cd1)
                    .addComponent(jTextField_codigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(jPanel_RGPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField_color, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_colorp)
                    .addComponent(jLabel_fregis)
                    .addComponent(jTextField_registro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_cd2)
                    .addComponent(jTextField_idProduc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(55, 55, 55)
                .addComponent(jButton_RegisProducto)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel_RGPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_mostrar)
                    .addComponent(jButton_edit)
                    .addComponent(jButton_guar)
                    .addComponent(jButton_elimi))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane6.addTab("REGISTROS DE NUEVO PRODCUTOS", jPanel_RGP);

        jPanel_BSCP.setBackground(new java.awt.Color(255, 255, 255));

        jLabel38.setFont(new java.awt.Font("Sitka Text", 3, 18)); // NOI18N
        jLabel38.setText("BUSCAR PRODUCTO");

        jLabel_cd3.setFont(new java.awt.Font("Sitka Text", 3, 14)); // NOI18N
        jLabel_cd3.setText("BUSCAR POR CODIGO :");

        jTextField_Buscodigo1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_Buscodigo1.setForeground(new java.awt.Color(102, 102, 102));
        jTextField_Buscodigo1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_Buscodigo1ActionPerformed(evt);
            }
        });

        jButton_buscodigo1.setText("BUSCAR");
        jButton_buscodigo1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_buscodigo1ActionPerformed(evt);
            }
        });

        jTable_buscarpro.setAutoCreateRowSorter(true);
        jTable_buscarpro.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Codigo", "Nombre de  Producto", "Cantidad", "Marca", "Color Producto", "Fecha Registro", "Precio Compra", "Precio Venta"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane5.setViewportView(jTable_buscarpro);
        if (jTable_buscarpro.getColumnModel().getColumnCount() > 0) {
            jTable_buscarpro.getColumnModel().getColumn(0).setPreferredWidth(50);
            jTable_buscarpro.getColumnModel().getColumn(1).setPreferredWidth(100);
            jTable_buscarpro.getColumnModel().getColumn(2).setPreferredWidth(150);
            jTable_buscarpro.getColumnModel().getColumn(3).setPreferredWidth(70);
            jTable_buscarpro.getColumnModel().getColumn(4).setPreferredWidth(100);
            jTable_buscarpro.getColumnModel().getColumn(5).setPreferredWidth(100);
            jTable_buscarpro.getColumnModel().getColumn(6).setPreferredWidth(120);
            jTable_buscarpro.getColumnModel().getColumn(7).setPreferredWidth(100);
            jTable_buscarpro.getColumnModel().getColumn(8).setPreferredWidth(100);
        }

        javax.swing.GroupLayout jPanel_BSCPLayout = new javax.swing.GroupLayout(jPanel_BSCP);
        jPanel_BSCP.setLayout(jPanel_BSCPLayout);
        jPanel_BSCPLayout.setHorizontalGroup(
            jPanel_BSCPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_BSCPLayout.createSequentialGroup()
                .addGroup(jPanel_BSCPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_BSCPLayout.createSequentialGroup()
                        .addGroup(jPanel_BSCPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel_BSCPLayout.createSequentialGroup()
                                .addGap(350, 350, 350)
                                .addComponent(jLabel38))
                            .addGroup(jPanel_BSCPLayout.createSequentialGroup()
                                .addGap(57, 57, 57)
                                .addComponent(jLabel_cd3, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField_Buscodigo1, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton_buscodigo1)))
                        .addGap(0, 347, Short.MAX_VALUE))
                    .addGroup(jPanel_BSCPLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane5)))
                .addContainerGap())
        );
        jPanel_BSCPLayout.setVerticalGroup(
            jPanel_BSCPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_BSCPLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel38)
                .addGap(45, 45, 45)
                .addGroup(jPanel_BSCPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_cd3)
                    .addComponent(jTextField_Buscodigo1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_buscodigo1))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane6.addTab("BUSQUEDA DE  PRODCUTOS", jPanel_BSCP);

        jPanel_GI.setBackground(new java.awt.Color(255, 255, 255));

        jLabel29.setFont(new java.awt.Font("Sitka Text", 3, 18)); // NOI18N
        jLabel29.setText("GESTION DE INVENTARIO");

        jButton_Gmostrar.setText("MOSTRAR");
        jButton_Gmostrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_GmostrarActionPerformed(evt);
            }
        });

        jTable_MostrarProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Codigo", "Nombre Producto", "Cantidad", "Marca", "Color Producto", "Fecha Registro", "Precio Compra", "Precio Venta"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane9.setViewportView(jTable_MostrarProductos);
        jTable_MostrarProductos.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        if (jTable_MostrarProductos.getColumnModel().getColumnCount() > 0) {
            jTable_MostrarProductos.getColumnModel().getColumn(0).setPreferredWidth(50);
            jTable_MostrarProductos.getColumnModel().getColumn(1).setPreferredWidth(100);
            jTable_MostrarProductos.getColumnModel().getColumn(2).setPreferredWidth(150);
            jTable_MostrarProductos.getColumnModel().getColumn(3).setPreferredWidth(70);
            jTable_MostrarProductos.getColumnModel().getColumn(4).setPreferredWidth(80);
            jTable_MostrarProductos.getColumnModel().getColumn(5).setPreferredWidth(100);
            jTable_MostrarProductos.getColumnModel().getColumn(6).setPreferredWidth(120);
            jTable_MostrarProductos.getColumnModel().getColumn(7).setPreferredWidth(100);
            jTable_MostrarProductos.getColumnModel().getColumn(8).setPreferredWidth(100);
        }

        javax.swing.GroupLayout jPanel_GILayout = new javax.swing.GroupLayout(jPanel_GI);
        jPanel_GI.setLayout(jPanel_GILayout);
        jPanel_GILayout.setHorizontalGroup(
            jPanel_GILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_GILayout.createSequentialGroup()
                .addContainerGap(332, Short.MAX_VALUE)
                .addComponent(jLabel29)
                .addGap(313, 313, 313))
            .addGroup(jPanel_GILayout.createSequentialGroup()
                .addGroup(jPanel_GILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_GILayout.createSequentialGroup()
                        .addGap(63, 63, 63)
                        .addComponent(jButton_Gmostrar, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel_GILayout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 843, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel_GILayout.setVerticalGroup(
            jPanel_GILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_GILayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel29)
                .addGap(64, 64, 64)
                .addComponent(jButton_Gmostrar)
                .addGap(33, 33, 33)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(132, Short.MAX_VALUE))
        );

        jTabbedPane6.addTab("GESTION DE INVENTARIO", jPanel_GI);

        javax.swing.GroupLayout jPanel_productosLayout = new javax.swing.GroupLayout(jPanel_productos);
        jPanel_productos.setLayout(jPanel_productosLayout);
        jPanel_productosLayout.setHorizontalGroup(
            jPanel_productosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_productosLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTabbedPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 897, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel_productosLayout.setVerticalGroup(
            jPanel_productosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_productosLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTabbedPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 525, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47))
        );

        jPanel_ventas.setBackground(new java.awt.Color(255, 255, 255));

        jTabbedPane7.setBackground(new java.awt.Color(255, 255, 255));

        jPanel_VE.setBackground(new java.awt.Color(255, 255, 255));

        jLabel30.setFont(new java.awt.Font("Sitka Text", 3, 18)); // NOI18N
        jLabel30.setText("REGISTRO DE VENTAS");

        javax.swing.GroupLayout jPanel_VELayout = new javax.swing.GroupLayout(jPanel_VE);
        jPanel_VE.setLayout(jPanel_VELayout);
        jPanel_VELayout.setHorizontalGroup(
            jPanel_VELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_VELayout.createSequentialGroup()
                .addContainerGap(357, Short.MAX_VALUE)
                .addComponent(jLabel30)
                .addGap(324, 324, 324))
        );
        jPanel_VELayout.setVerticalGroup(
            jPanel_VELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_VELayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel30)
                .addContainerGap(486, Short.MAX_VALUE))
        );

        jTabbedPane7.addTab("REGISTRO DE VENTAS", jPanel_VE);

        javax.swing.GroupLayout jPanel_LVLayout = new javax.swing.GroupLayout(jPanel_LV);
        jPanel_LV.setLayout(jPanel_LVLayout);
        jPanel_LVLayout.setHorizontalGroup(
            jPanel_LVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 892, Short.MAX_VALUE)
        );
        jPanel_LVLayout.setVerticalGroup(
            jPanel_LVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 528, Short.MAX_VALUE)
        );

        jTabbedPane7.addTab("LISTADO DE VENTAS", jPanel_LV);

        javax.swing.GroupLayout jPanel_BCPROLayout = new javax.swing.GroupLayout(jPanel_BCPRO);
        jPanel_BCPRO.setLayout(jPanel_BCPROLayout);
        jPanel_BCPROLayout.setHorizontalGroup(
            jPanel_BCPROLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 892, Short.MAX_VALUE)
        );
        jPanel_BCPROLayout.setVerticalGroup(
            jPanel_BCPROLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 528, Short.MAX_VALUE)
        );

        jTabbedPane7.addTab("BUSQUEDA DE PRODUCTOS", jPanel_BCPRO);

        javax.swing.GroupLayout jPanel_ventasLayout = new javax.swing.GroupLayout(jPanel_ventas);
        jPanel_ventas.setLayout(jPanel_ventasLayout);
        jPanel_ventasLayout.setHorizontalGroup(
            jPanel_ventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_ventasLayout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addComponent(jTabbedPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 897, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel_ventasLayout.setVerticalGroup(
            jPanel_ventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_ventasLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jTabbedPane7)
                .addContainerGap())
        );

        jPanel_proveedores.setBackground(new java.awt.Color(255, 255, 255));

        jTabbedPane8.setBackground(new java.awt.Color(255, 255, 255));

        jPanel_RPRO.setBackground(new java.awt.Color(255, 255, 255));

        jLabel39.setFont(new java.awt.Font("Sitka Text", 3, 18)); // NOI18N
        jLabel39.setText("REGISTRO DE PROVEEDORES ");

        jLabel4.setFont(new java.awt.Font("Sitka Text", 3, 14)); // NOI18N
        jLabel4.setText("RAZON SOCIAL  :");

        jLabel5.setFont(new java.awt.Font("Sitka Text", 3, 14)); // NOI18N
        jLabel5.setText("RUC  :");

        jLabel6.setFont(new java.awt.Font("Sitka Text", 3, 14)); // NOI18N
        jLabel6.setText("DIRECCION DEL PROVEEDOR :");

        jLabel8.setFont(new java.awt.Font("Sitka Text", 3, 14)); // NOI18N
        jLabel8.setText("CORREO ELECTRONICO :");

        jLabel19.setFont(new java.awt.Font("Sitka Text", 3, 14)); // NOI18N
        jLabel19.setText("NOMBRE DEL PROVEEDOR  :");

        jLabel24.setFont(new java.awt.Font("Sitka Text", 3, 14)); // NOI18N
        jLabel24.setText("TELEFONO DEL PROVEEDOR :");

        jTextField_DirProveedor.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_DirProveedor.setForeground(new java.awt.Color(102, 102, 102));

        jTextField_TelProveedor.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_TelProveedor.setForeground(new java.awt.Color(102, 102, 102));

        jTextField_EmailProveedor.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_EmailProveedor.setForeground(new java.awt.Color(102, 102, 102));

        jTextField_RazonSocial.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_RazonSocial.setForeground(new java.awt.Color(102, 102, 102));
        jTextField_RazonSocial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_RazonSocialActionPerformed(evt);
            }
        });

        jTextField_NomProveedor.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_NomProveedor.setForeground(new java.awt.Color(102, 102, 102));
        jTextField_NomProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_NomProveedorActionPerformed(evt);
            }
        });

        jTextField_FRegistro.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_FRegistro.setForeground(new java.awt.Color(102, 102, 102));
        jTextField_FRegistro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_FRegistroActionPerformed(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("Sitka Text", 3, 14)); // NOI18N
        jLabel23.setText("F/REGISTRO :");

        jTextField_RUC.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_RUC.setForeground(new java.awt.Color(102, 102, 102));
        jTextField_RUC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_RUCActionPerformed(evt);
            }
        });

        jLabel25.setFont(new java.awt.Font("Sitka Text", 3, 14)); // NOI18N
        jLabel25.setText("CONTACTO :");

        jTextField_Contacto.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_Contacto.setForeground(new java.awt.Color(102, 102, 102));
        jTextField_Contacto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_ContactoActionPerformed(evt);
            }
        });

        jButton_GuardProveedor.setText("GUARDAR PROVEEDOR");
        jButton_GuardProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_GuardProveedorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel_RPROLayout = new javax.swing.GroupLayout(jPanel_RPRO);
        jPanel_RPRO.setLayout(jPanel_RPROLayout);
        jPanel_RPROLayout.setHorizontalGroup(
            jPanel_RPROLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_RPROLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel_RPROLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(jLabel19)
                    .addComponent(jLabel24)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                .addGroup(jPanel_RPROLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextField_TelProveedor, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                    .addComponent(jTextField_DirProveedor)
                    .addComponent(jTextField_EmailProveedor)
                    .addComponent(jTextField_NomProveedor))
                .addGap(45, 45, 45)
                .addGroup(jPanel_RPROLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel23)
                    .addComponent(jLabel25))
                .addGap(18, 18, 18)
                .addGroup(jPanel_RPROLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextField_FRegistro)
                    .addComponent(jTextField_RazonSocial)
                    .addComponent(jTextField_RUC, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                    .addComponent(jTextField_Contacto))
                .addContainerGap(28, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_RPROLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel_RPROLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_RPROLayout.createSequentialGroup()
                        .addComponent(jLabel39)
                        .addGap(288, 288, 288))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_RPROLayout.createSequentialGroup()
                        .addComponent(jButton_GuardProveedor)
                        .addGap(329, 329, 329))))
        );
        jPanel_RPROLayout.setVerticalGroup(
            jPanel_RPROLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_RPROLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel39)
                .addGap(71, 71, 71)
                .addGroup(jPanel_RPROLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(jLabel4)
                    .addComponent(jTextField_RazonSocial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField_NomProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel_RPROLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jTextField_DirProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField_RUC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel_RPROLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(jTextField_TelProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23)
                    .addComponent(jTextField_FRegistro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel_RPROLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_RPROLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField_EmailProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel8))
                    .addGroup(jPanel_RPROLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField_Contacto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel25)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 119, Short.MAX_VALUE)
                .addComponent(jButton_GuardProveedor)
                .addGap(107, 107, 107))
        );

        jTabbedPane8.addTab("REGISTRO DE PROVEEDORES", jPanel_RPRO);

        jPanel_BCPROVE.setBackground(new java.awt.Color(255, 255, 255));

        jLabel43.setFont(new java.awt.Font("Sitka Text", 3, 18)); // NOI18N
        jLabel43.setText("BUSQUEDA DE PROVEEDORES ");

        jTable_proveedor.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Nombre Proveedor", "Direccion Proveedor", "Telefono Proveedor", "Correo Proveedor", "Razon Social", "Ruc", "Fecha Registro", "Contacto"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane6.setViewportView(jTable_proveedor);
        if (jTable_proveedor.getColumnModel().getColumnCount() > 0) {
            jTable_proveedor.getColumnModel().getColumn(0).setPreferredWidth(20);
            jTable_proveedor.getColumnModel().getColumn(1).setPreferredWidth(110);
            jTable_proveedor.getColumnModel().getColumn(2).setPreferredWidth(110);
            jTable_proveedor.getColumnModel().getColumn(3).setPreferredWidth(110);
            jTable_proveedor.getColumnModel().getColumn(4).setPreferredWidth(110);
            jTable_proveedor.getColumnModel().getColumn(6).setPreferredWidth(80);
            jTable_proveedor.getColumnModel().getColumn(7).setPreferredWidth(80);
            jTable_proveedor.getColumnModel().getColumn(8).setPreferredWidth(80);
        }

        jTextField_IDProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_IDProveedorActionPerformed(evt);
            }
        });

        jLabel26.setText("NOMBRE DEL PROVEERDOR :");

        jButton_Proveedor.setText("BUSCAR");
        jButton_Proveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ProveedorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel_BCPROVELayout = new javax.swing.GroupLayout(jPanel_BCPROVE);
        jPanel_BCPROVE.setLayout(jPanel_BCPROVELayout);
        jPanel_BCPROVELayout.setHorizontalGroup(
            jPanel_BCPROVELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_BCPROVELayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel_BCPROVELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_BCPROVELayout.createSequentialGroup()
                        .addGroup(jPanel_BCPROVELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel43)
                            .addGroup(jPanel_BCPROVELayout.createSequentialGroup()
                                .addComponent(jLabel26)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField_IDProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton_Proveedor)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 872, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel_BCPROVELayout.setVerticalGroup(
            jPanel_BCPROVELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_BCPROVELayout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(jLabel43)
                .addGap(32, 32, 32)
                .addGroup(jPanel_BCPROVELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField_IDProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_Proveedor)
                    .addComponent(jLabel26))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane8.addTab("BUSQUEDA DE PROVEEDORES", jPanel_BCPROVE);

        javax.swing.GroupLayout jPanel_proveedoresLayout = new javax.swing.GroupLayout(jPanel_proveedores);
        jPanel_proveedores.setLayout(jPanel_proveedoresLayout);
        jPanel_proveedoresLayout.setHorizontalGroup(
            jPanel_proveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_proveedoresLayout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addComponent(jTabbedPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 897, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel_proveedoresLayout.setVerticalGroup(
            jPanel_proveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_proveedoresLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jTabbedPane8)
                .addContainerGap())
        );

        jPanel_reportes.setBackground(new java.awt.Color(255, 255, 255));

        jTabbedPane9.setBackground(new java.awt.Color(255, 255, 255));

        jPanel_GRV.setBackground(new java.awt.Color(255, 255, 255));

        jLabel32.setFont(new java.awt.Font("Sitka Text", 3, 18)); // NOI18N
        jLabel32.setText("REGISTRO DE REPORTES DE VENTAS");

        javax.swing.GroupLayout jPanel_GRVLayout = new javax.swing.GroupLayout(jPanel_GRV);
        jPanel_GRV.setLayout(jPanel_GRVLayout);
        jPanel_GRVLayout.setHorizontalGroup(
            jPanel_GRVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_GRVLayout.createSequentialGroup()
                .addGap(260, 260, 260)
                .addComponent(jLabel32)
                .addContainerGap(285, Short.MAX_VALUE))
        );
        jPanel_GRVLayout.setVerticalGroup(
            jPanel_GRVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_GRVLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel32)
                .addContainerGap(479, Short.MAX_VALUE))
        );

        jTabbedPane9.addTab("GENERACION DE REPORTES DE VENTAS", jPanel_GRV);

        jPanel_GRVEN.setBackground(new java.awt.Color(255, 255, 255));

        jLabel40.setFont(new java.awt.Font("Sitka Text", 3, 18)); // NOI18N
        jLabel40.setText("REGISTRO DE REPORTES DE CITAS");

        javax.swing.GroupLayout jPanel_GRVENLayout = new javax.swing.GroupLayout(jPanel_GRVEN);
        jPanel_GRVEN.setLayout(jPanel_GRVENLayout);
        jPanel_GRVENLayout.setHorizontalGroup(
            jPanel_GRVENLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_GRVENLayout.createSequentialGroup()
                .addGap(260, 260, 260)
                .addComponent(jLabel40)
                .addContainerGap(305, Short.MAX_VALUE))
        );
        jPanel_GRVENLayout.setVerticalGroup(
            jPanel_GRVENLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_GRVENLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel40)
                .addContainerGap(479, Short.MAX_VALUE))
        );

        jTabbedPane9.addTab("GENERACION DE REPORTES DE CITAS", jPanel_GRVEN);

        jPanel_GRI.setBackground(new java.awt.Color(255, 255, 255));

        jLabel41.setFont(new java.awt.Font("Sitka Text", 3, 18)); // NOI18N
        jLabel41.setText("REGISTRO DE REPORTES DE INVENTARIO");

        javax.swing.GroupLayout jPanel_GRILayout = new javax.swing.GroupLayout(jPanel_GRI);
        jPanel_GRI.setLayout(jPanel_GRILayout);
        jPanel_GRILayout.setHorizontalGroup(
            jPanel_GRILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_GRILayout.createSequentialGroup()
                .addGap(260, 260, 260)
                .addComponent(jLabel41)
                .addContainerGap(238, Short.MAX_VALUE))
        );
        jPanel_GRILayout.setVerticalGroup(
            jPanel_GRILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_GRILayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel41)
                .addContainerGap(479, Short.MAX_VALUE))
        );

        jTabbedPane9.addTab("GENERACION DE REPORTES DE INVENTARIO", jPanel_GRI);

        javax.swing.GroupLayout jPanel_reportesLayout = new javax.swing.GroupLayout(jPanel_reportes);
        jPanel_reportes.setLayout(jPanel_reportesLayout);
        jPanel_reportesLayout.setHorizontalGroup(
            jPanel_reportesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_reportesLayout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addComponent(jTabbedPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 897, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel_reportesLayout.setVerticalGroup(
            jPanel_reportesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_reportesLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jTabbedPane9)
                .addContainerGap())
        );

        jPanel_usuarios.setBackground(new java.awt.Color(255, 255, 255));

        jTabbedPane10.setBackground(new java.awt.Color(255, 255, 255));

        jPanel_RNUS.setBackground(new java.awt.Color(255, 255, 255));

        jLabel42.setFont(new java.awt.Font("Sitka Text", 3, 18)); // NOI18N
        jLabel42.setText("REGISTRO DE USUARIOS");

        jLabel16.setBackground(new java.awt.Color(102, 102, 102));
        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel16.setText("Nombre y Apellidos :");

        fname1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        fname1.setForeground(new java.awt.Color(102, 102, 102));

        jLabel17.setBackground(new java.awt.Color(102, 102, 102));
        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel17.setText("Correo :");

        emailAddress1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        emailAddress1.setForeground(new java.awt.Color(102, 102, 102));

        jLabel18.setBackground(new java.awt.Color(102, 102, 102));
        jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel18.setText("Contraseña :");

        pass2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        pass2.setForeground(new java.awt.Color(102, 102, 102));

        jLabel_Rol1.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        jLabel_Rol1.setText("Seleccionar cargo :");

        Roles1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Administrador", "Optometrista", "Recepcionista" }));
        Roles1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Roles1ActionPerformed(evt);
            }
        });

        SignUpBtn2.setBackground(new java.awt.Color(0, 102, 102));
        SignUpBtn2.setForeground(new java.awt.Color(255, 255, 255));
        SignUpBtn2.setText("Crear Cuenta");
        SignUpBtn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SignUpBtn2SignUpBtnActionPerformed(evt);
            }
        });

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID_Usuario", "Nombre", "Correo", "Rol", "Cargo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane4.setViewportView(jTable2);

        jLabel22.setFont(new java.awt.Font("Sitka Text", 3, 18)); // NOI18N
        jLabel22.setText("LISTA DE USUARIOS ");

        jButton_MostrarUsuarios.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        jButton_MostrarUsuarios.setText("Mostrar Usuarios");
        jButton_MostrarUsuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_MostrarUsuariosActionPerformed(evt);
            }
        });

        jButton_editar1.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        jButton_editar1.setText("Editar");
        jButton_editar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_editar1ActionPerformed(evt);
            }
        });

        jButton_guardar1.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        jButton_guardar1.setText("Guardar");
        jButton_guardar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_guardar1ActionPerformed(evt);
            }
        });

        jButton_eliminar1.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        jButton_eliminar1.setText("Eliminar");
        jButton_eliminar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_eliminar1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel_RNUSLayout = new javax.swing.GroupLayout(jPanel_RNUS);
        jPanel_RNUS.setLayout(jPanel_RNUSLayout);
        jPanel_RNUSLayout.setHorizontalGroup(
            jPanel_RNUSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_RNUSLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(jPanel_RNUSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel_Rol1)
                    .addGroup(jPanel_RNUSLayout.createSequentialGroup()
                        .addGroup(jPanel_RNUSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addComponent(jLabel17)
                            .addComponent(jLabel18))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel_RNUSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Roles1, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(emailAddress1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fname1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pass2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 169, Short.MAX_VALUE)
                .addComponent(SignUpBtn2, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(90, 90, 90))
            .addGroup(jPanel_RNUSLayout.createSequentialGroup()
                .addGap(291, 291, 291)
                .addComponent(jLabel42)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_RNUSLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_RNUSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel_RNUSLayout.createSequentialGroup()
                        .addComponent(jLabel22)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton_MostrarUsuarios)
                        .addGap(18, 18, 18)
                        .addComponent(jButton_editar1, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton_guardar1)
                        .addGap(18, 18, 18)
                        .addComponent(jButton_eliminar1, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane4))
                .addGap(18, 18, 18))
        );
        jPanel_RNUSLayout.setVerticalGroup(
            jPanel_RNUSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_RNUSLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel42)
                .addGap(40, 40, 40)
                .addGroup(jPanel_RNUSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fname1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addGroup(jPanel_RNUSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_RNUSLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(jPanel_RNUSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(emailAddress1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17)))
                    .addGroup(jPanel_RNUSLayout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(SignUpBtn2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(30, 30, 30)
                .addGroup(jPanel_RNUSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pass2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18))
                .addGap(31, 31, 31)
                .addGroup(jPanel_RNUSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_Rol1)
                    .addComponent(Roles1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(60, 60, 60)
                .addGroup(jPanel_RNUSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(jButton_MostrarUsuarios)
                    .addComponent(jButton_editar1)
                    .addComponent(jButton_guardar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_eliminar1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(49, Short.MAX_VALUE))
        );

        jTabbedPane10.addTab("REGISTRO DE USUARIOS Y ASIGNAR ROL ", jPanel_RNUS);

        javax.swing.GroupLayout jPanel_usuariosLayout = new javax.swing.GroupLayout(jPanel_usuarios);
        jPanel_usuarios.setLayout(jPanel_usuariosLayout);
        jPanel_usuariosLayout.setHorizontalGroup(
            jPanel_usuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_usuariosLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTabbedPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 897, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel_usuariosLayout.setVerticalGroup(
            jPanel_usuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_usuariosLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTabbedPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 542, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel_ayuda.setBackground(new java.awt.Color(255, 255, 255));

        jTabbedPane11.setBackground(new java.awt.Color(255, 255, 255));

        jPanel_CPS.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Tahoma", 3, 18)); // NOI18N
        jLabel1.setText("Telefonos de Atención :  965888756");

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/Imagen de WhatsApp 2024-10-08 a las 07.12.48_9c1f8414.jpg"))); // NOI18N

        jLabel3.setFont(new java.awt.Font("Tahoma", 3, 18)); // NOI18N
        jLabel3.setText("Estamos para  ayudarte");

        javax.swing.GroupLayout jPanel_CPSLayout = new javax.swing.GroupLayout(jPanel_CPS);
        jPanel_CPS.setLayout(jPanel_CPSLayout);
        jPanel_CPSLayout.setHorizontalGroup(
            jPanel_CPSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_CPSLayout.createSequentialGroup()
                .addGroup(jPanel_CPSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_CPSLayout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 338, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel_CPSLayout.createSequentialGroup()
                        .addGap(72, 72, 72)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 691, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(129, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_CPSLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(329, 329, 329))
        );
        jPanel_CPSLayout.setVerticalGroup(
            jPanel_CPSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_CPSLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 392, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addGap(24, 24, 24))
        );

        jTabbedPane11.addTab("CONTACTO PARA SOPORTE", jPanel_CPS);

        javax.swing.GroupLayout jPanel_ayudaLayout = new javax.swing.GroupLayout(jPanel_ayuda);
        jPanel_ayuda.setLayout(jPanel_ayudaLayout);
        jPanel_ayudaLayout.setHorizontalGroup(
            jPanel_ayudaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_ayudaLayout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addComponent(jTabbedPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 897, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel_ayudaLayout.setVerticalGroup(
            jPanel_ayudaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_ayudaLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTabbedPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 561, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel_containerLayout = new javax.swing.GroupLayout(jPanel_container);
        jPanel_container.setLayout(jPanel_containerLayout);
        jPanel_containerLayout.setHorizontalGroup(
            jPanel_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_containerLayout.createSequentialGroup()
                .addComponent(jPanel_menu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel_containerLayout.createSequentialGroup()
                        .addComponent(jPanel_dashboard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 2, Short.MAX_VALUE))))
            .addGroup(jPanel_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_containerLayout.createSequentialGroup()
                    .addGap(0, 221, Short.MAX_VALUE)
                    .addComponent(jPanel_citas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(jPanel_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_containerLayout.createSequentialGroup()
                    .addContainerGap(215, Short.MAX_VALUE)
                    .addComponent(jPanel_pacientes, javax.swing.GroupLayout.PREFERRED_SIZE, 907, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
            .addGroup(jPanel_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_containerLayout.createSequentialGroup()
                    .addGap(0, 215, Short.MAX_VALUE)
                    .addComponent(jPanel_productos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(jPanel_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_containerLayout.createSequentialGroup()
                    .addGap(0, 206, Short.MAX_VALUE)
                    .addComponent(jPanel_ventas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(jPanel_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_containerLayout.createSequentialGroup()
                    .addGap(0, 210, Short.MAX_VALUE)
                    .addComponent(jPanel_proveedores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(jPanel_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_containerLayout.createSequentialGroup()
                    .addGap(0, 209, Short.MAX_VALUE)
                    .addComponent(jPanel_reportes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(jPanel_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_containerLayout.createSequentialGroup()
                    .addGap(0, 215, Short.MAX_VALUE)
                    .addComponent(jPanel_usuarios, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(jPanel_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_containerLayout.createSequentialGroup()
                    .addGap(0, 208, Short.MAX_VALUE)
                    .addComponent(jPanel_ayuda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel_containerLayout.setVerticalGroup(
            jPanel_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel_menu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel_containerLayout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel_dashboard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_containerLayout.createSequentialGroup()
                    .addGap(0, 85, Short.MAX_VALUE)
                    .addComponent(jPanel_citas, javax.swing.GroupLayout.PREFERRED_SIZE, 580, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(jPanel_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_containerLayout.createSequentialGroup()
                    .addGap(0, 99, Short.MAX_VALUE)
                    .addComponent(jPanel_pacientes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(jPanel_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_containerLayout.createSequentialGroup()
                    .addContainerGap(89, Short.MAX_VALUE)
                    .addComponent(jPanel_productos, javax.swing.GroupLayout.PREFERRED_SIZE, 565, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
            .addGroup(jPanel_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_containerLayout.createSequentialGroup()
                    .addGap(0, 78, Short.MAX_VALUE)
                    .addComponent(jPanel_ventas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(jPanel_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_containerLayout.createSequentialGroup()
                    .addGap(0, 79, Short.MAX_VALUE)
                    .addComponent(jPanel_proveedores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(jPanel_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_containerLayout.createSequentialGroup()
                    .addGap(0, 81, Short.MAX_VALUE)
                    .addComponent(jPanel_reportes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(jPanel_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_containerLayout.createSequentialGroup()
                    .addContainerGap(91, Short.MAX_VALUE)
                    .addComponent(jPanel_usuarios, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(10, 10, 10)))
            .addGroup(jPanel_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_containerLayout.createSequentialGroup()
                    .addGap(0, 82, Short.MAX_VALUE)
                    .addComponent(jPanel_ayuda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel_container, javax.swing.GroupLayout.DEFAULT_SIZE, 1140, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel_container, javax.swing.GroupLayout.DEFAULT_SIZE, 675, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void LogoutBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LogoutBtnActionPerformed
   
        Login LoginFrame = new Login();
        LoginFrame.setVisible(true);
        LoginFrame.pack();
        LoginFrame.setLocationRelativeTo(null); 
        this.dispose();
     
    }//GEN-LAST:event_LogoutBtnActionPerformed

    private void jButton_RegisProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_RegisProductoActionPerformed
                                                                                                                                                                  
                                                                                                                
        try {
            // 1. Obtener los datos de los campos de texto (JTextField)
            String nombreProducto = jTextField_producto.getText();
            String marca = jTextField_marca.getText();
            String color = jTextField_color.getText();
            
            // Validación de campos de texttidad.getText().isEmpty() || jTextField_compra.getText().iso (cantidad, precioCompra, precioVenta)
            if (jTextField_cantidad.getText().isEmpty() || jTextField_compra.getText().isEmpty() || jTextField_venta.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Por favor, llena todos los campos.");
                return; // Detener la ejecución si los campos están vacíos
            }
            
            int cantidad = Integer.parseInt(jTextField_cantidad.getText()); 
            double precioCompra = Double.parseDouble(jTextField_compra.getText());
            double precioVenta = Double.parseDouble(jTextField_venta.getText());
            String codigo = jTextField_codigo.getText(); 
            // Bloquear el campo jTextField_idProduc
            // jTextField_idProduc.setEditable(false);
                
            // 2. Obtener la fecha del JTextField (con manejo de errores)
            String fechaRegistro = jTextField_registro.getText(); // Obtén la fecha como texto

            // 3. Establecer la conexión con la base de datos
            String SUrl = "jdbc:mysql://localhost:3306/opticagestioncitas"; 
            String SUser = "root"; 
            String SPass = ""; 
            Connection conexion = DriverManager.getConnection(SUrl, SUser, SPass);

            // 4. Preparar las sentencias INSERT
            PreparedStatement stmtProductos = conexion.prepareStatement(
                "INSERT INTO Productos (codigo, nombre_producto, marca, precio_com, precio_ven, cantidad, fecha_Registro, color) VALUES (?, ?, ?, ?, ?, ?, ?, ?)", 
                Statement.RETURN_GENERATED_KEYS); // Habilita obtener el ID generado

            

            // 5. Asignar los valores a la sentencia preparada para Productos
            stmtProductos.setString(1, codigo);
            stmtProductos.setString(2, nombreProducto);
            stmtProductos.setString(3, marca);
            stmtProductos.setDouble(4, precioCompra);
            stmtProductos.setDouble(5, precioVenta);
            stmtProductos.setInt(6, cantidad);
            stmtProductos.setString(7, fechaRegistro);
            stmtProductos.setString(8, color);

           // 6. Ejecutar la sentencia para insertar el producto en la base de datos
                int filasAfectadasProductos = stmtProductos.executeUpdate();

                // 7. Obtener el ID del producto recién insertado
                int idProduc = 0;
                try (ResultSet rs = stmtProductos.getGeneratedKeys()) {
                    if (rs.next()) {
                        idProduc = rs.getInt(1);
                        // Mostrar el ID en el jTextField_idProduc
                        jTextField_idProduc.setText(String.valueOf(idProduc));
                    }
                }

            // 10. Verificar si se insertó el producto y código correctamente
            if (filasAfectadasProductos > 0) {
                // Si se insertó correctamente, muestra un mensaje al usuario
                JOptionPane.showMessageDialog(null, "¡Producto registrado exitosamente!");
                
                // Limpiar los campos de texto
                jTextField_producto.setText("");
                jTextField_marca.setText("");
                jTextField_color.setText("");
                jTextField_cantidad.setText("");
                jTextField_compra.setText("");
                jTextField_venta.setText("");
                jTextField_codigo.setText(""); 
                jTextField_registro.setText(""); // Limpia el campo de fecha
                jTextField_idProduc.setText(""); // Limpia el campo ID
                
            } else {
                // Si no se insertó, muestra un mensaje de error
                JOptionPane.showMessageDialog(null, "Error al registrar el producto.");
            }

            // 11. Cerrar la conexión y la sentencia
            stmtProductos.close();
            conexion.close();

        } catch (SQLException e) {
            // Manejar excepciones:  Imprime el error o muestra un mensaje al usuario
            System.err.println("Error al registrar el producto: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error al registrar el producto: " + e.getMessage());
        } catch (NumberFormatException e) {
            // Manejar excepciones:  Imprime el error o muestra un mensaje al usuario
            System.err.println("Error al convertir los datos a números: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error al convertir los datos a números: " + e.getMessage());
        }



    }//GEN-LAST:event_jButton_RegisProductoActionPerformed

    private void jButton_buscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_buscarActionPerformed
        String criterioBusqueda = NOP.getText(); // Obtiene el criterio de búsqueda del campo de texto

        try (Connection conexion = conectarDB();
             PreparedStatement stmt = conexion.prepareStatement(
                     "SELECT * FROM Pacientes WHERE Nombre LIKE ? OR Apellido LIKE ? OR Correo LIKE ? OR Telefono LIKE ?")) {

            // Verificar si el criterio de búsqueda es vacío
            if (criterioBusqueda.isEmpty()) {
                // No se muestra nada si el criterio de búsqueda está vacío
                return;
            }

            // Si el criterio de búsqueda no está vacío, buscar coincidencias
            stmt.setString(1, "%" + criterioBusqueda + "%");
            stmt.setString(2, "%" + criterioBusqueda + "%");
            stmt.setString(3, "%" + criterioBusqueda + "%");
            stmt.setString(4, "%" + criterioBusqueda + "%");

            ResultSet rs = stmt.executeQuery();

            // Obtener el modelo de la tabla
            DefaultTableModel model = (DefaultTableModel) jTable_BSC.getModel();
            // Limpiar el modelo de la tabla
            model.setRowCount(0);

            // Verificar si el ID del usuario es válido
            if (Login.variableGlobalIdUsuario == 0) {
                JOptionPane.showMessageDialog(this, "No se ha encontrado el ID del usuario", "Error", JOptionPane.ERROR_MESSAGE);
                return; // Sale de la función si el ID no es válido
            }

            // Obtener el nombre del usuario (fuera del bucle)
            String nombreUsuario = "";
            try (PreparedStatement stmtUsuario = conexion.prepareStatement(
                    "SELECT nombre_usuario FROM Usuarios WHERE id = ?")) {
                stmtUsuario.setInt(1, (int) Login.variableGlobalIdUsuario);
                ResultSet rsUsuario = stmtUsuario.executeQuery();
                if (rsUsuario.next()) {
                    nombreUsuario = rsUsuario.getString("nombre_usuario");
                }
            }

            // Agregar los datos al modelo de la tabla
            while (rs.next()) {
                int idPaciente = rs.getInt("Id"); // Obtener el ID del paciente
                String nombre = rs.getString("Nombre");
                String apellido = rs.getString("Apellido");
                String correo = rs.getString("Correo");
                String telefono = rs.getString("Telefono");
                String direccion = rs.getString("Direccion");
                String sexo = rs.getString("Sexo");
                Date fechaNacimiento = rs.getDate("Fecha_Nacimiento"); // Obtener la fecha de nacimiento

                // Agregar la fila al modelo de la tabla
                model.addRow(new Object[]{idPaciente, nombre, apellido, correo, telefono, direccion, sexo, fechaNacimiento, nombreUsuario}); // Agrega el ID al principio del array
            }

        } catch (SQLException ex) {
            // Manejar la excepción
            JOptionPane.showMessageDialog(this, "Error al buscar pacientes: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            
        }
   
    

    }//GEN-LAST:event_jButton_buscarActionPerformed

    private void jButton_registrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_registrarActionPerformed
       // Mostrar el ID del usuario logueado en jTextField_IdA
        jTextField_IdA.setText(String.valueOf(Login.variableGlobalIdUsuario)); 

        try (Connection conexion = conectarDB();
             PreparedStatement stmt = conexion.prepareStatement(
                     "INSERT INTO Pacientes (Nombre, Apellido, Correo, Telefono, Direccion, Sexo, Fecha_Nacimiento, Usuario_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {

            // Validación de entrada (Ejemplo: formato de correo electrónico)
            String correo = CE.getText();
            if (!correo.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                JOptionPane.showMessageDialog(this, "Error: correo electronico invalido.", "Error", JOptionPane.ERROR_MESSAGE);
                return; // Detente si el correo electrónico no es válido
            }

            stmt.setString(1, NP.getText());
            stmt.setString(2, AP.getText());
            stmt.setString(3, correo);
            stmt.setString(4, TL.getText());
            stmt.setString(5, DC.getText());  // Dirección 
            stmt.setString(6, jComboBox_Sexo.getSelectedItem().toString());  // Sexo from JComboBox

            // Obtener la fecha de nacimiento del jDateChooser
            java.util.Date fechaNacimiento = jDateChooser_Nacimiento.getDate();
            // Establecer la fecha de nacimiento en la sentencia preparada
            stmt.setDate(7, new java.sql.Date(fechaNacimiento.getTime()));

            // int idUsuarioLogueado = variableGlobalIdUsuario; 
            stmt.setInt(8, (int) Login.variableGlobalIdUsuario); // Asignar el ID del usuario al parámetro 8

            int filasAfectadas = stmt.executeUpdate();

            // Manejar el resultado de la inserción
            if (filasAfectadas > 0) {
                // Se insertó el paciente correctamente
                JOptionPane.showMessageDialog(this, "Paciente registrado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                // Limpiar los campos del formulario
                NP.setText("");
                AP.setText("");
                CE.setText("");
                TL.setText("");
                DC.setText("");  // Limpiar dirección
                jComboBox_Sexo.setSelectedIndex(0); // Restablece la selección del JComboBox 
                jDateChooser_Nacimiento.setDate(null);  // Limpiar fecha de nacimiento
                // jTextField_IdA.setText(""); // Limpiar el campo de ID (ya no es necesario)
                
     
                // Actualizar la tabla jTable_EEAG
                cargarTablaEEAG(); // Llama al método para cargar la tabla
            } else {
                // No se pudo insertar el paciente
                JOptionPane.showMessageDialog(this, "Error al registrar el paciente.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            // Manejar la excepción
            JOptionPane.showMessageDialog(this, "Error al registrar el paciente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            // Bloquear el campo jTextField_IdA
            jTextField_IdA.setEditable(false);
            // Bloquear el campo jTextField_IdPa
            jTextField_IdPa.setEditable(false);
            boolean enable;
           
        } 
    
    }//GEN-LAST:event_jButton_registrarActionPerformed

    private void jButton_LimpiarBusquedaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_LimpiarBusquedaActionPerformed
       // Obtener el modelo de la tabla
            DefaultTableModel model = (DefaultTableModel) jTable_BSC.getModel();

            // Limpiar el modelo de la tabla (elimina todas las filas)
            model.setRowCount(0);

            // Limpiar el campo de texto de búsqueda
            NOP.setText("");

    }//GEN-LAST:event_jButton_LimpiarBusquedaActionPerformed

    private void jButton_editarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_editarActionPerformed
 
        int filaSeleccionada = jTable_EEAG.getSelectedRow();
        if (filaSeleccionada != -1) {
            // Habilitar el modo de edición
            modoEdicion = true;

            // Obtener los datos de la fila seleccionada
            String idPacienteStr = jTable_EEAG.getValueAt(filaSeleccionada, 0).toString();  // Obtén el ID como String
            int idPaciente = Integer.parseInt(idPacienteStr); // Convierte a entero
            String nombre = (String) jTable_EEAG.getValueAt(filaSeleccionada, 1);
            String apellido = (String) jTable_EEAG.getValueAt(filaSeleccionada, 2);
            String correo = (String) jTable_EEAG.getValueAt(filaSeleccionada, 3);
            String telefono = (String) jTable_EEAG.getValueAt(filaSeleccionada, 4);
            String direccion = (String) jTable_EEAG.getValueAt(filaSeleccionada, 5); 
            String sexo = (String) jTable_EEAG.getValueAt(filaSeleccionada, 6);     
            java.sql.Date fechaNacimiento = (java.sql.Date) jTable_EEAG.getValueAt(filaSeleccionada, 7);

            // Llenar los campos de texto con los datos de la fila
            jTextField_IdPa.setText(idPacienteStr); // Asigna el ID a jTextField_IdPa
            NP.setText(nombre);
            AP.setText(apellido);
            CE.setText(correo);
            TL.setText(telefono);
            DC.setText(direccion); 
            jDateChooser_Nacimiento.setDate(fechaNacimiento); 

            // Habilitar los campos de texto para edición
            NP.setEditable(true);
            AP.setEditable(true);
            CE.setEditable(true);
            TL.setEditable(true);
            DC.setEditable(true); 
            jDateChooser_Nacimiento.setEnabled(true); 

            // Bloquear el campo jTextField_IdA
            jTextField_IdA.setEditable(false); 
            // Establecer el ID del usuario logueado en jTextField_IdA
            jTextField_IdA.setText(String.valueOf(Login.variableGlobalIdUsuario)); 

        } else {
            JOptionPane.showMessageDialog(Dashboard_Form.this, "Seleccione una fila para editar.", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_jButton_editarActionPerformed

    private void jButton_eliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_eliminarActionPerformed
        
            int filaSeleccionada = jTable_EEAG.getSelectedRow();
            if (filaSeleccionada != -1) {
                // Obtener el ID del paciente de la columna 0
                int idPaciente = (int) jTable_EEAG.getValueAt(filaSeleccionada, 0); 

                // Obtener el nombre y apellido para el mensaje de confirmación (opcional)
                String nombre = (String) jTable_EEAG.getValueAt(filaSeleccionada, 1); // Asume que el nombre está en la columna 1
                String apellido = (String) jTable_EEAG.getValueAt(filaSeleccionada, 2); // Asume que el apellido está en la columna 2

                // Confirmar la eliminación
                int opcion = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea eliminar este paciente (ID: " + idPaciente + ", Nombre: " + nombre + " " + apellido + ")?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
                if (opcion == JOptionPane.YES_OPTION) {
                    try (Connection conexion = conectarDB()) {
                        // Iniciar una transacción
                        conexion.setAutoCommit(false); 

                        try {
                            // Eliminar las citas relacionadas
                            try (PreparedStatement stmtDeleteCitas = conexion.prepareStatement(
                                    "DELETE FROM Citas WHERE paciente_id = ?")) {
                                stmtDeleteCitas.setInt(1, idPaciente);
                                stmtDeleteCitas.executeUpdate();
                            }

                            // Eliminar el paciente
                            try (PreparedStatement stmtDeletePaciente = conexion.prepareStatement(
                                    "DELETE FROM Pacientes WHERE Id = ?")) {
                                stmtDeletePaciente.setInt(1, idPaciente);
                                int filasAfectadas = stmtDeletePaciente.executeUpdate();
                                if (filasAfectadas > 0) {
                                    // Se eliminó el paciente correctamente
                                    JOptionPane.showMessageDialog(this, "Paciente eliminado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                                    // Actualizar la tabla jTable_EEAG
                                    cargarTablaEEAG();
                                } else {
                                    // No se pudo eliminar el paciente
                                    JOptionPane.showMessageDialog(this, "Error al eliminar el paciente.", "Error", JOptionPane.ERROR_MESSAGE);
                                }
                            }

                            // Confirmar la transacción
                            conexion.commit(); 
                        } catch (SQLException ex) {
                            // Revertir la transacción en caso de error
                            conexion.rollback(); 
                            JOptionPane.showMessageDialog(this, "Error al eliminar el paciente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                            // Registrar la excepción en un archivo de registro (opcional)
                            // ...
                        }
                    } catch (SQLException ex) {
                        // Manejar la excepción
                        JOptionPane.showMessageDialog(this, "Error al eliminar el paciente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        // Registrar la excepción en un archivo de registro (opcional)
                        // ...
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione una fila para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_jButton_eliminarActionPerformed

    private void jButton_guardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_guardarActionPerformed
               
           // Obtener los datos de los campos de texto
            String nombre = NP.getText();
            String apellido = AP.getText();
            String correo = CE.getText();
            String telefono = TL.getText();
            String direccion = DC.getText(); 
            String sexo = jComboBox_Sexo.getSelectedItem().toString(); 
            java.util.Date fechaNacimiento = jDateChooser_Nacimiento.getDate();

            // Convertir java.util.Date a java.sql.Date
            java.sql.Date sqlFechaNacimiento = new java.sql.Date(fechaNacimiento.getTime());

            // Obtener el ID del usuario de la variable global
            int idUsuario = (int) Login.variableGlobalIdUsuario;

            // Mostrar el ID del usuario en jTextField_IdA
            jTextField_IdA.setText(String.valueOf(idUsuario));

            // Obtener el ID del paciente desde jTextField_IdPa
            int idPaciente = Integer.parseInt(jTextField_IdPa.getText()); 

            // Actualizar la fila en la base de datos
            try (Connection conexion = conectarDB();
                 PreparedStatement stmt = conexion.prepareStatement(
                         "UPDATE Pacientes SET Nombre = ?, apellido = ?, correo = ?, telefono = ?, direccion = ?, sexo = ?, fecha_Nacimiento = ? WHERE Id = ?")) {
                
                stmt.setString(1, nombre);
                stmt.setString(2, apellido);
                stmt.setString(3, correo);
                stmt.setString(4, telefono);
                stmt.setString(5, direccion);
                stmt.setString(6, sexo);
                stmt.setDate(7, sqlFechaNacimiento);
                stmt.setInt(8, idPaciente);

                int filasAfectadas = stmt.executeUpdate();

                if (filasAfectadas > 0) {
                    // Se actualizó el paciente correctamente
                    JOptionPane.showMessageDialog(Dashboard_Form.this, "Paciente actualizado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    // Actualizar la tabla jTable_EEAG (actualizar la fila)
                    // ... (tu código para actualizar la tabla jTable_EEAG)
                    // Puedes usar el método fireTableDataChanged() del modelo de la tabla
                } else {
                    // No se pudo actualizar el paciente
                    JOptionPane.showMessageDialog(Dashboard_Form.this, "Error al actualizar el paciente.", "Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (SQLException ex) {
                // Manejar la excepción
                JOptionPane.showMessageDialog(Dashboard_Form.this, "Error al actualizar el paciente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }

            // Limpiar los campos de texto
            NP.setText("");
            AP.setText("");
            CE.setText("");
            TL.setText("");
            DC.setText("");  
            jComboBox_Sexo.setSelectedIndex(0); 
            jDateChooser_Nacimiento.setDate(null); 
            // jTextField_IdA.setText(""); 
            // jTextField_IdPa.setText("");  // Puedes limpiar jTextField_IdPa si deseas
    
    }//GEN-LAST:event_jButton_guardarActionPerformed

    private void jButton_MostrarPacientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_MostrarPacientesActionPerformed
        // TODO add your handling code here:
        cargarTablaEEAG();
    }//GEN-LAST:event_jButton_MostrarPacientesActionPerformed

    private void jButton_RcitaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_RcitaActionPerformed
        try (Connection conexion = conectarDB();
            PreparedStatement stmtCita = conexion.prepareStatement(
                "INSERT INTO Citas (paciente_id, usuario_id, servicio_id, fecha_cita, hora, observaciones, tipo_pago) VALUES (?, ?, ?, ?, ?, ?, ?)")) {

            int idPaciente = Integer.parseInt(ID.getText()); // Obtiene el ID del paciente
            int idUsuario = Integer.parseInt(ID_user.getText()); // Obtiene el usuario_id
            int idServicio = Integer.parseInt(jTextField_IDServicio.getText()); // Obtiene el ID del servicio del jTextField

            // Validación de campos (opcional, para evitar errores al insertar)
            if (idPaciente == 0 || idUsuario == 0 || idServicio == 0) {
                JOptionPane.showMessageDialog(this, "Falta información esencial para registrar la cita.", "Error", JOptionPane.ERROR_MESSAGE);
                return; // Detener la ejecución si falta información
            }

            // Obtener la fecha de la cita
            java.util.Date fechaCita = jDateChooser_Cita.getDate(); // Obtener la fecha de la cita del jDateChooser
            if (fechaCita == null) {
                JOptionPane.showMessageDialog(this, "Por favor, selecciona una fecha para la cita.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Obtener la hora de la cita (asumiendo que tienes un JTextField llamado Hora)
            String horaCitaStr = Hora.getText();

            // Convertir la hora a un Timestamp
            java.sql.Timestamp horaCita = null;
            if (horaCitaStr != null && !horaCitaStr.isEmpty()) {
                try {
                    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss"); // Formato de tiempo 24 horas
                    java.util.Date hora = formatter.parse(horaCitaStr);
                    horaCita = new java.sql.Timestamp(hora.getTime()); // Convierte a Timestamp
                } catch (ParseException e) {
                    JOptionPane.showMessageDialog(this, "Formato de hora inválido. Por favor, ingrese una hora en el formato HH:mm:ss", "Error", JOptionPane.ERROR_MESSAGE);
                    return; // Detener la ejecución si hay un error de formato
                }
            } else {
                // Si no se proporciona una hora, puedes usar la hora actual o un valor por defecto
                horaCita = new java.sql.Timestamp(new java.util.Date().getTime()); // Hora actual
                // horaCita = new java.sql.Timestamp(new java.util.Date(2024, 9, 30, 10, 0, 0).getTime()); // Hora predeterminada
            }

            // Convertir la fecha a java.sql.Date
            java.sql.Date sqlFechaCita = new java.sql.Date(fechaCita.getTime()); // Corrección: usa getTime()

            // Obtener los datos adicionales
            int edad = Integer.parseInt(Edad.getText()); // Obtener edad del paciente
            String observaciones = jTextArea_Obse.getText(); // Obtener observaciones
            // Obtener el tipo de pago
            String tipoPago = (String) jComboBox_pago.getSelectedItem();

            // Preparar la sentencia
            stmtCita.setInt(1, idPaciente);
            stmtCita.setInt(2, idUsuario);
            stmtCita.setInt(3, idServicio);
            stmtCita.setDate(4, sqlFechaCita); // Establecer la fecha de la cita
            stmtCita.setTimestamp(5, horaCita); // Establecer la hora de la cita
            stmtCita.setString(6, observaciones); // Establecer observaciones
            stmtCita.setString(7, tipoPago); // Establecer el tipo de pago

            // Ejecutar la consulta de inserción
            int filasAfectadas = stmtCita.executeUpdate();

            // Mostrar mensaje de éxito o error
            if (filasAfectadas > 0) {
                JOptionPane.showMessageDialog(this, "Cita registrada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

                // Limpiar los campos del formulario
                ID.setText("");
                ID_user.setText("");
                jTextField_IDServicio.setText("");
                jDateChooser_Cita.setDate(null); // Limpiar el jDateChooser
                Hora.setText("");
                Edad.setText("");
                jTextArea_Obse.setText("");
                jComboBox_pago.setSelectedIndex(0); // Restablecer el ComboBox
                Nom.setText("");
                Ape.setText("");
                Dire.setText("");
                Tele.setText("");
                Ema.setText("");
                Sex.setText("");
                FN.setText(""); // Limpiar el jTextField
                // ...
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar la cita.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            // Manejar la excepción
            JOptionPane.showMessageDialog(this, "Error al registrar la cita: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            // Manejar la excepción si el ID no es un número válido
            JOptionPane.showMessageDialog(this, "Ingrese un ID válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton_RcitaActionPerformed

    private void jTextField_precioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_precioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_precioActionPerformed

    private void jCombo_ServicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCombo_ServicioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCombo_ServicioActionPerformed

    private void DireActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DireActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_DireActionPerformed

    private void ApeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ApeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ApeActionPerformed

    private void EmaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EmaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_EmaActionPerformed

    private void NomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NomActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NomActionPerformed

    private void TeleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TeleActionPerformed

    }//GEN-LAST:event_TeleActionPerformed

    private void jButton_IDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_IDActionPerformed
        try (Connection conexion = conectarDB();
            PreparedStatement stmt = conexion.prepareStatement(
                "SELECT * FROM Pacientes WHERE id = ?")) {

            int idPaciente = Integer.parseInt(ID.getText()); // Obtener el ID del paciente del campo de texto
            stmt.setInt(1, idPaciente);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Llenar los campos con los datos del paciente
                Nom.setText(rs.getString("nombre"));
                Ape.setText(rs.getString("apellido"));
                Dire.setText(rs.getString("direccion"));
                Tele.setText(rs.getString("telefono"));
                Ema.setText(rs.getString("correo"));
                Sex.setText(rs.getString("sexo"));
                // Obtener la fecha de nacimiento como Date y formatearla
                java.sql.Date fechaNacimiento = rs.getDate("fecha_nacimiento");
                if (fechaNacimiento != null) { // Verifica si la fecha de nacimiento no es null
                    SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
                    FN.setText(rs.getDate("fecha_nacimiento").toString());

                    // Calcular la edad
                    int edad = calcularEdad(fechaNacimiento);
                    Edad.setText(String.valueOf(edad)); // Mostrar la edad en el jTextField edad
                }
                ID_user.setText(String.valueOf(rs.getInt("usuario_id"))); // Mostrar usuario_id

                // Bloquear los campos para que no se puedan editar
                Nom.setEditable(false);
                Ape.setEditable(false);
                Dire.setEditable(false);
                Tele.setEditable(false);
                Ema.setEditable(false);
                Sex.setEditable(false);
                FN.setEditable(false); // Deshabilitar el jtextfield
                ID_user.setEditable(false);
                Edad.setEditable(false); // Deshabilitar el jTextField edad

            } else {
                // Mostrar mensaje de error si no se encuentra el paciente
                JOptionPane.showMessageDialog(this, "Paciente no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            // Manejar la excepción
            JOptionPane.showMessageDialog(this, "Error al buscar paciente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            // Manejar la excepción si el ID no es un número válido
            JOptionPane.showMessageDialog(this, "Ingrese un ID válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton_IDActionPerformed

    private void jButton_Id_11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_Id_11ActionPerformed
            try (Connection conexion = conectarDB();
            PreparedStatement stmtPaciente = conexion.prepareStatement(
                "SELECT * FROM Pacientes WHERE id = ?")) {

            int idPaciente;
            try {
                idPaciente = Integer.parseInt(jTextField_Id11.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Ingrese un ID válido (número entero).", "Error", JOptionPane.ERROR_MESSAGE);
                return; // Sale de la función si el ID no es válido
            }

            stmtPaciente.setInt(1, idPaciente);

            ResultSet rsPaciente = stmtPaciente.executeQuery();

            if (rsPaciente.next()) {
                // Llenar los campos con los datos del paciente
                jTextField_Nom1.setText(rsPaciente.getString("nombre"));
                jTextField_Ape1.setText(rsPaciente.getString("apellido"));
                jTextField_Dire1.setText(rsPaciente.getString("direccion"));
                jTextField_Tf.setText(rsPaciente.getString("telefono"));
                jTextField_em.setText(rsPaciente.getString("correo"));
                jTextField_S.setText(rsPaciente.getString("sexo"));
                java.sql.Date fechaNacimiento = rsPaciente.getDate("fecha_nacimiento");
                if (fechaNacimiento != null) {
                    SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
                    jTextField_FN1.setText(formatoFecha.format(fechaNacimiento));
                    int edad = calcularEdad(fechaNacimiento);
                    jTextField_E.setText(String.valueOf(edad));
                } else {
                    jTextField_FN1.setText("");
                    jTextField_E.setText("");
                }

                // Buscar la cita asociada al paciente
                try (PreparedStatement stmtCita = conexion.prepareStatement(
                    "SELECT * FROM Citas WHERE paciente_id = ?")) {
                    stmtCita.setInt(1, idPaciente);
                    ResultSet rsCita = stmtCita.executeQuery();

                    if (rsCita.next()) {
                        // Obtener la hora y fecha de la cita
                        String horaCita = rsCita.getString("hora");
                        String fechaCita = rsCita.getString("fecha_cita");
                        jTextField_Hora1.setText(horaCita);
                        jTextField_RC.setText(fechaCita);

                        // Obtener los datos del servicio
                        String observaciones = rsCita.getString("observaciones");
                        jTextArea_Obse12.setText(observaciones);
                    } else {
                        JOptionPane.showMessageDialog(this, "No se encontró una cita asociada al paciente.", "Información", JOptionPane.INFORMATION_MESSAGE);
                    }
                } 
            } else {
                JOptionPane.showMessageDialog(this, "Paciente no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            }

            // Bloquear los campos para que no se puedan editar
            jTextField_Nom1.setEditable(false);
            jTextField_Ape1.setEditable(false);
            jTextField_Dire1.setEditable(false);
            jTextField_Tf.setEditable(false);
            jTextField_em.setEditable(false);
            jTextField_S.setEditable(false);
            jTextField_FN1.setEditable(false); 
            jTextField_E.setEditable(false); 
            jTextField_Hora1.setEditable(false); 
            jTextField_RC.setEditable(false); 
            jTextArea_Obse12.setEditable(false); 
            // ... (bloquear otros campos según sea necesario)


        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al buscar paciente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un ID válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
                  
    }//GEN-LAST:event_jButton_Id_11ActionPerformed

    private void jTextField_TfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_TfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_TfActionPerformed

    private void jTextField_Nom1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_Nom1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_Nom1ActionPerformed

    private void jTextField_emActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_emActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_emActionPerformed

    private void jTextField_Ape1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_Ape1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_Ape1ActionPerformed

    private void jTextField_Dire1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_Dire1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_Dire1ActionPerformed

    private void jButton_RHCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_RHCActionPerformed
            
        try (Connection conexion = conectarDB();
        PreparedStatement stmt = conexion.prepareStatement(
            "INSERT INTO Historial_Medico (paciente_id, fecha, diagnostico, tratamiento, fecha_cita, hora, observaciones) VALUES (?, ?, ?, ?, ?, ?, ?)",
            Statement.RETURN_GENERATED_KEYS)) { // Cambia para obtener el ID generado

            int idPaciente = Integer.parseInt(jTextField_Id11.getText());
            String diagnostico = jTextArea_Diag14.getText();
            String tratamiento = jTextArea_Trata13.getText();
            String observaciones = jTextArea_Obse12.getText(); 

            // Lee los datos desde los campos de texto
            String horaCita = jTextField_Hora1.getText();
            String fechaCita = jTextField_RC.getText(); 

            // Convierte a java.sql.Date
            java.sql.Date fechaCitaObjeto = new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(fechaCita).getTime());
            Time horaCitaObjeto = Time.valueOf(horaCita);

            // Establecer los valores de los parámetros en la consulta
            stmt.setInt(1, idPaciente);
            // No se necesita fecha, ya que la columna 'fecha' parece estar autogenerada
            stmt.setDate(2, fechaCitaObjeto); 
            stmt.setString(3, diagnostico);
            stmt.setString(4, tratamiento);
            stmt.setTimestamp(5, new Timestamp(fechaCitaObjeto.getTime())); 
            stmt.setTime(6, horaCitaObjeto); 
            stmt.setString(7, observaciones);

            // Ejecutar la consulta para guardar los datos
            int filasInsertadas = stmt.executeUpdate(); 

            if (filasInsertadas > 0) {
                // Obtener el ID generado
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int idHistorial = generatedKeys.getInt(1);
                        JOptionPane.showMessageDialog(this, "Historial Médico guardado correctamente. ID: " + idHistorial, "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Error al guardar el Historial Médico. No se pudo obtener el ID.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                     // Limpiar los campos después de guardar el historial
                        jTextField_Nom1.setText("");
                        jTextField_Ape1.setText("");
                        jTextField_Dire1.setText("");
                        jTextField_Tf.setText("");
                        jTextField_em.setText("");
                        jTextField_S.setText("");
                        jTextField_FN1.setText(""); 
                        jTextField_E.setText("");
                        jTextField_Id11.setText("");
                        jTextArea_Diag14.setText("");
                        jTextArea_Trata13.setText("");
                        jTextArea_Obse12.setText("");
                        jTextField_Hora1.setText("");
                        jTextField_RC.setText("");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar el Historial Médico.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar el Historial Médico: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "Error al convertir la fecha y hora de la cita: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        

    }//GEN-LAST:event_jButton_RHCActionPerformed

    private void jTextField_RCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_RCActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_RCActionPerformed

    private void jButton_BusHistoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_BusHistoriaActionPerformed
           try (Connection conexion = conectarDB();
            PreparedStatement stmt = conexion.prepareStatement(
                "SELECT * FROM Pacientes WHERE nombre LIKE ?")) { // Busca por nombre

            String nombrePaciente = jTextField1.getText(); 
            stmt.setString(1, "%" + nombrePaciente + "%"); // Agrega '%' para buscar por subcadena

            try (ResultSet rs = stmt.executeQuery()) {
                // Crea el modelo de datos para el JTable
                DefaultTableModel modeloTabla = new DefaultTableModel();
                modeloTabla.addColumn("ID");
                modeloTabla.addColumn("Nombre");
                modeloTabla.addColumn("Apellidos");
                modeloTabla.addColumn("Dirección");
                modeloTabla.addColumn("Correo");
                modeloTabla.addColumn("Sexo");
                modeloTabla.addColumn("Generar Historial"); // Agrega la columna para el botón

                // Agrega los datos del paciente a la tabla
                while (rs.next()) { // Usa while para agregar todas las filas
                    modeloTabla.addRow(new Object[] {
                        rs.getInt("id"), // Corregido: Ahora se usa "id"
                        rs.getString("nombre"), // Reemplaza "nombre_paciente" por el nombre de la columna real
                        rs.getString("apellido"), // Reemplaza "apellidos_paciente" por el nombre de la columna real
                        rs.getString("direccion"), // Reemplaza "direccion_paciente" por el nombre de la columna real
                        rs.getString("correo"), // Reemplaza "email_paciente" por el nombre de la columna real
                        rs.getString("sexo"), // Reemplaza "sexo_paciente" por el nombre de la columna real
                        new JButton("Generar Historial") // Crea un nuevo botón para cada fila
                    });
                }

                // Asigna el modelo de datos a la tabla jTable_Historias
                jTable_Historias.setModel(modeloTabla);

            /*    // Agrega el botón "Generar Historial" a cada fila
                for (int i = 0; i < jTable_Historias.getRowCount(); i++) {
                    JButton generarHistorialButton = (JButton) jTable_Historias.getValueAt(i, 6); // Obtén el botón de la tabla
                    generarHistorialButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            int idPaciente = (int) jTable_Historias.getValueAt(i, 0); // Obtén el ID del paciente de la fila actual
                            generarHistorial(idPaciente); // Llama a la función para generar el historial
                        }
                    });
                }

                if (jTable_Historias.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(this, "No se encontró ningún historial para el nombre ingresado.", "Error", JOptionPane.ERROR_MESSAGE);
                }*/
            }
       } catch (SQLException ex) {
           JOptionPane.showMessageDialog(this, "Error al buscar el historial médico: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
       }
    }//GEN-LAST:event_jButton_BusHistoriaActionPerformed

    private void jButton_editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_editActionPerformed
                                                     
        try {
            // 1. Obtener la fila seleccionada del JTable
            int filaSeleccionada = jTable_Productos.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(null, "Por favor, selecciona un producto para editar.");
                return; // Si no se seleccionó ninguna fila, salir
            }

            // 2. Obtener el ID del producto seleccionado
            int idProducto = (int) jTable_Productos.getValueAt(filaSeleccionada, 0); // Suponiendo que ID es la columna 0

            // 3. Establecer la conexión con la base de datos
            String SUrl = "jdbc:mysql://localhost:3306/opticagestioncitas"; 
            String SUser = "root"; 
            String SPass = ""; 
            Connection conexion = DriverManager.getConnection(SUrl, SUser, SPass);

            // 4. Preparar la sentencia SELECT para obtener los datos del producto
            PreparedStatement stmtMostrarProducto = conexion.prepareStatement(
                "SELECT * FROM Productos WHERE ID = ?");
            stmtMostrarProducto.setInt(1, idProducto); // Asignar el ID del producto
             
            // 5. Ejecutar la sentencia para obtener los datos
            ResultSet rs = stmtMostrarProducto.executeQuery();

            // 8. Llenar los campos de texto con los datos del producto
            if (rs.next()) {
                // Obtener los valores de las columnas y asignarlos a los JTextField
                jTextField_idProduc.setText(String.valueOf(rs.getInt("ID")));
                jTextField_producto.setText(rs.getString("nombre_producto"));
                jTextField_cantidad.setText(String.valueOf(rs.getInt("cantidad")));
                jTextField_marca.setText(rs.getString("marca"));
                jTextField_color.setText(rs.getString("color"));
                jTextField_registro.setText(rs.getString("fecha_Registro"));
                jTextField_compra.setText(String.valueOf(rs.getDouble("precio_com")));
                jTextField_venta.setText(String.valueOf(rs.getDouble("precio_ven")));
                // Obtener el código directamente de la tabla Productos
                jTextField_codigo.setText(rs.getString("codigo")); 
                
                // Bloquear los campos para que no se puedan editar
                jTextField_codigo.setEditable(false); 
            }

            // 9. Cerrar la conexión y la sentencia
            stmtMostrarProducto.close();
            conexion.close();

        } catch (SQLException e) {
            // Manejar excepciones:  Imprime el error o muestra un mensaje al usuario
            System.err.println("Error al obtener los datos del producto: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error al obtener los datos del producto: " + e.getMessage());
        } 
    

    }//GEN-LAST:event_jButton_editActionPerformed

    private void jButton_mostrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_mostrarActionPerformed
       // TODO add your handling code here:
        try {
            // 1. Establecer la conexión con la base de datos
            String SUrl = "jdbc:mysql://localhost:3306/opticagestioncitas"; 
            String SUser = "root"; 
            String SPass = ""; 
            Connection conexion = DriverManager.getConnection(SUrl, SUser, SPass);

            // 2. Preparar la sentencia SELECT para Productos
            PreparedStatement stmtMostrarProductos = conexion.prepareStatement(
                "SELECT ID, codigo, nombre_producto, cantidad, marca, color, fecha_Registro, precio_com, precio_ven FROM Productos"); 

            // 3. Ejecutar la sentencia para obtener los datos de Productos
            ResultSet rsProductos = stmtMostrarProductos.executeQuery();

            // 4. Mostrar los datos en un JTable (o en otro componente visual)
            // Crear un modelo de tabla
            DefaultTableModel modeloTabla = new DefaultTableModel();
            // Agregar las columnas deseadas
            modeloTabla.addColumn("ID");
            modeloTabla.addColumn("Código");
            modeloTabla.addColumn("Nombre Producto");
            modeloTabla.addColumn("Cantidad");
            modeloTabla.addColumn("Marca");
            modeloTabla.addColumn("Color");
            modeloTabla.addColumn("Fecha Registro");
            modeloTabla.addColumn("Precio Compra");
            modeloTabla.addColumn("Precio Venta");

            // Iterar sobre los resultados de Productos
            while (rsProductos.next()) {
                Object[] fila = new Object[9]; // 9 columnas
                fila[0] = rsProductos.getInt("ID");
                fila[1] = rsProductos.getString("codigo"); 
                fila[2] = rsProductos.getString("nombre_producto"); // Corregido para usar "nom_producto"
                fila[3] = rsProductos.getInt("cantidad");
                fila[4] = rsProductos.getString("marca");
                fila[5] = rsProductos.getString("color");
                fila[6] = rsProductos.getString("fecha_Registro"); 
                fila[7] = rsProductos.getDouble("precio_com");
                fila[8] = rsProductos.getDouble("precio_ven");

                modeloTabla.addRow(fila);
            }
            // Asignar el modelo de tabla al JTable 
            jTable_Productos.setModel(modeloTabla);
            
            // Ajustar el tamaño de las columnas automáticamente
            jTable_Productos.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            
            // Ajustar el tamaño de las columnas automáticamente
            jTable_Productos.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

            // Establecer ancho preferido para las demás columnas
            jTable_Productos.getColumnModel().getColumn(0).setPreferredWidth(50); // Columna ID
            jTable_Productos.getColumnModel().getColumn(1).setPreferredWidth(100); // Columna Código
            jTable_Productos.getColumnModel().getColumn(2).setPreferredWidth(150); // Columna Nombre Producto
            jTable_Productos.getColumnModel().getColumn(3).setPreferredWidth(70); // Columna Cantidad
            jTable_Productos.getColumnModel().getColumn(4).setPreferredWidth(100); // Columna Marca
            jTable_Productos.getColumnModel().getColumn(5).setPreferredWidth(80); // Columna Color
            jTable_Productos.getColumnModel().getColumn(6).setPreferredWidth(120); // Columna Fecha Registro
            jTable_Productos.getColumnModel().getColumn(7).setPreferredWidth(100); // Columna Precio Compra
            jTable_Productos.getColumnModel().getColumn(8).setPreferredWidth(100); // Columna Precio Venta

            
            
            
            // 7. Cerrar la conexión y la sentencia
            stmtMostrarProductos.close();
            conexion.close();

        } catch (SQLException e) {
            // Manejar excepciones:  Imprime el error o muestra un mensaje al usuario
            System.err.println("Error al mostrar los productos: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error al mostrar los productos: " + e.getMessage());
        }   
    
    }//GEN-LAST:event_jButton_mostrarActionPerformed

    private void jButton_guarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_guarActionPerformed
        // TODO add your handling code here:
                                                 
        try {
            // 1. Obtener el ID del producto a actualizar (ya debe estar en jTextField_idProduc)
            int idProducto = Integer.parseInt(jTextField_idProduc.getText());

            // 2. Obtener los nuevos datos de los campos de texto
            String nombreProducto = jTextField_producto.getText();
            int cantidad = Integer.parseInt(jTextField_cantidad.getText());
            String marca = jTextField_marca.getText();
            String color = jTextField_color.getText();
            String fechaRegistro = jTextField_registro.getText(); 
            double precioCompra = Double.parseDouble(jTextField_compra.getText());
            double precioVenta = Double.parseDouble(jTextField_venta.getText());

            // 3. Establecer la conexión con la base de datos
            String SUrl = "jdbc:mysql://localhost:3306/opticagestioncitas"; 
            String SUser = "root"; 
            String SPass = ""; 
            Connection conexion = DriverManager.getConnection(SUrl, SUser, SPass);

            // 4. Preparar la sentencia UPDATE para actualizar los datos del producto
            PreparedStatement stmtActualizar = conexion.prepareStatement(
                "UPDATE Productos SET nombre_producto = ?, cantidad = ?, marca = ?, color = ?, fecha_Registro = ?, precio_com = ?, precio_ven = ? WHERE ID = ?");

            // 5. Asignar los valores a la sentencia UPDATE
            stmtActualizar.setString(1, nombreProducto);
            stmtActualizar.setInt(2, cantidad);
            stmtActualizar.setString(3, marca);
            stmtActualizar.setString(4, color);
            stmtActualizar.setString(5, fechaRegistro); 
            stmtActualizar.setDouble(6, precioCompra);
            stmtActualizar.setDouble(7, precioVenta);
            stmtActualizar.setInt(8, idProducto);

            // 6. Ejecutar la sentencia UPDATE
            int filasAfectadas = stmtActualizar.executeUpdate();

            // 7. Verificar si se actualizó el producto correctamente
            if (filasAfectadas > 0) {
                // Si se actualizó correctamente, muestra un mensaje al usuario
                JOptionPane.showMessageDialog(null, "¡Producto actualizado exitosamente!");

                // Limpiar los campos de texto
                jTextField_idProduc.setText("");
                jTextField_producto.setText("");
                jTextField_cantidad.setText("");
                jTextField_marca.setText("");
                jTextField_color.setText("");
                jTextField_registro.setText(""); 
                jTextField_compra.setText("");
                jTextField_venta.setText("");
                jTextField_codigo.setText(""); 

                // Bloquear el campo jTextField_idProduc 
                jTextField_idProduc.setEditable(false); 
                 jTextField_codigo.setEditable(false); 
                // Deshabilitar el botón de registro
                jButton_RegisProducto.setEnabled(false); 

                // Actualizar el JTable
                // (Necesitas llamar al método para actualizar el JTable después de guardar)
                // Puedes agregar una llamada a tu método de actualización de JTable aquí

            } else {
                // Si no se actualizó, muestra un mensaje de error
                JOptionPane.showMessageDialog(null, "Error al actualizar el producto.");
            }

            // 8. Cerrar la conexión y la sentencia
            stmtActualizar.close();
            conexion.close();

        } catch (SQLException e) {
            // Manejar excepciones:  Imprime el error o muestra un mensaje al usuario
            System.err.println("Error al actualizar el producto: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error al actualizar el producto: " + e.getMessage());
        }

    }//GEN-LAST:event_jButton_guarActionPerformed

    private void jButton_elimiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_elimiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton_elimiActionPerformed

    private void Roles1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Roles1ActionPerformed
         
    }//GEN-LAST:event_Roles1ActionPerformed

    private void SignUpBtn2SignUpBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SignUpBtn2SignUpBtnActionPerformed
        // System.out.println ("Regístrese btn hizo clic");
        String nombre_usuario, email, contraseña, query;
        String SUrl, SUser, SPass;
        SUrl = "jdbc:mysql://localhost:3306/opticagestioncitas";
        SUser = "root";
        SPass = "";
        int rolId = getRoleId(Roles1.getSelectedItem().toString()); // Obtener el ID del rol seleccionado
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Dashboard_Form.class.getName()).log(Level.SEVERE, null, ex);
        }

        Connection con = null;
        try {
            con = DriverManager.getConnection(SUrl, SUser, SPass);
        } catch (SQLException ex) {
            Logger.getLogger(Dashboard_Form.class.getName()).log(Level.SEVERE, null, ex);
        }
        Statement st = null;
        try {
            st = con.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(Dashboard_Form.class.getName()).log(Level.SEVERE, null, ex);
        }
        if ("".equals(fname1.getText())) { // Reemplaza fname1 por nombre
            JOptionPane.showMessageDialog(new JFrame(), "Se requiere nombre completo", "Error",
                JOptionPane.ERROR_MESSAGE);
        } else if ("".equals(emailAddress1.getText())) {
            JOptionPane.showMessageDialog(new JFrame(), "Se requiere dirección de correo electrónico", "Error",
                JOptionPane.ERROR_MESSAGE);
        } else if ("".equals(pass2.getText())) {
            JOptionPane.showMessageDialog(new JFrame(), "Se requiere contraseña", "Error",
                JOptionPane.ERROR_MESSAGE);
        } else {

            nombre_usuario = fname1.getText(); // Reemplaza fname1 por nombre
            email = emailAddress1.getText();
            contraseña = pass2.getText();
            System.out.println(contraseña);

            // Verificar si el nombre de usuario o el correo electrónico ya existen
            query = "SELECT COUNT(*) FROM Usuarios WHERE nombre_usuario = '" + nombre_usuario + "' OR email = '" + email + "'";
            ResultSet rs = null;
            try {
                rs = st.executeQuery(query);
                if (rs.next()) {
                    int count = rs.getInt(1);
                    if (count > 0) {
                        JOptionPane.showMessageDialog(new JFrame(), "El nombre de usuario o el correo electrónico ya existe", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    } else {
                        query = "INSERT INTO Usuarios(nombre_usuario, email, contraseña, rol_id)" +
                                " VALUES('" + nombre_usuario + "', '" + email + "' , '" + contraseña + "', " + rolId + ")";
                        try {
                            st.execute(query);
                            JOptionPane.showMessageDialog(new JFrame(), "Cuenta creada exitosamente", "Éxito",
                                JOptionPane.INFORMATION_MESSAGE);
                            // Limpiar los campos después del registro exitoso
                            fname1.setText(""); // Reemplaza fname1 por nombre
                            emailAddress1.setText("");
                            pass2.setText("");
                        } catch (SQLException ex) {
                            Logger.getLogger(Dashboard_Form.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(Dashboard_Form.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (rs != null) rs.close();
                    if (st != null) st.close();
                    if (con != null) con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Dashboard_Form.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }//GEN-LAST:event_SignUpBtn2SignUpBtnActionPerformed

    private void jButton_MostrarUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_MostrarUsuariosActionPerformed
            
            try (Connection conexion = conectarDB();
            PreparedStatement stmt = conexion.prepareStatement(
                    "SELECT id, nombre_usuario, email, rol_id FROM Usuarios")) { // Elimina la columna 'contraseña'

           ResultSet rs = stmt.executeQuery();

           // Obtener el modelo de la tabla
           DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
           // Limpiar el modelo de la tabla
           model.setRowCount(0);

           // Agregar los datos al modelo de la tabla
           while (rs.next()) {
               int id = rs.getInt("id");
               String nombreUsuario = rs.getString("nombre_usuario");
               String email = rs.getString("email"); // Elimina la columna 'contraseña'
               int rolId = rs.getInt("rol_id");
            // Obtener el nombre del rol
                   String nombreRol = "";
                   try (PreparedStatement stmtRol = conexion.prepareStatement(
                           "SELECT nombre_Rol FROM Roles WHERE id = ?")) {
                       stmtRol.setInt(1, rolId);
                       ResultSet rsRol = stmtRol.executeQuery();
                       if (rsRol.next()) {
                           nombreRol = rsRol.getString("nombre_Rol");
                       }
                   }

                   // Agregar la fila al modelo de la tabla
                   model.addRow(new Object[]{id, nombreUsuario, email, rolId , nombreRol}); 
               }

           } catch (SQLException ex) {
               // Manejar la excepción
               JOptionPane.showMessageDialog(this, "Error al cargar usuarios: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
           }

    }//GEN-LAST:event_jButton_MostrarUsuariosActionPerformed

    private void jButton_editar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_editar1ActionPerformed
        // TODO add your handling code here:
        // Obtener la fila seleccionada
        int filaSeleccionada = jTable2.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un usuario para editar", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Obtener los datos de la fila seleccionada
        int id = (int) jTable2.getValueAt(filaSeleccionada, 0);
        String nombreUsuario = (String) jTable2.getValueAt(filaSeleccionada, 1);
        String email = (String) jTable2.getValueAt(filaSeleccionada, 2);
        int rolId = (int) jTable2.getValueAt(filaSeleccionada, 3); // Obtener el rolId

        // Mostrar los datos en los jTextField
        fname1.setText(nombreUsuario);
        emailAddress1.setText(email);

        // Mostrar el nombre del rol en el JComboBox
        try (Connection conexion = conectarDB();
             PreparedStatement stmt = conexion.prepareStatement(
                     "SELECT nombre_Rol FROM Roles WHERE id = ?")) {
            stmt.setInt(1, rolId);
            ResultSet rsRol = stmt.executeQuery();
            if (rsRol.next()) {
                Roles1.setSelectedItem(rsRol.getString("nombre_Rol"));
            } else {
                // Mostrar un mensaje informativo si no se encuentra el rol
                JOptionPane.showMessageDialog(this, "No se encontró el nombre del rol para este usuario.", "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            // Manejar la excepción
            JOptionPane.showMessageDialog(this, "Error al cargar roles: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Obtener la contraseña y mostrarla en jTextField_pass2
        try (Connection conexion = conectarDB();
             PreparedStatement stmt = conexion.prepareStatement(
                     "SELECT contraseña FROM Usuarios WHERE id = ?")) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String contraseña = rs.getString("contraseña");
                pass2.setText(contraseña); // Mostrar la contraseña
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar la contraseña: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Activa el modo de edición (si es necesario)
        modoEdicion = true; 


    
    }//GEN-LAST:event_jButton_editar1ActionPerformed

    private void jButton_guardar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_guardar1ActionPerformed
        
        if (modoEdicion) {
        // Obtener los datos de los campos de texto
        String nombreUsuario = fname1.getText();
        String email = emailAddress1.getText();
        String contraseña = pass2.getText();

        // Obtener el rolId desde el JComboBox
        int rolId = 0;
        try {
            rolId = getRoleId(Roles1.getSelectedItem().toString());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al obtener el rolId: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return; // Detener la ejecución si no se puede obtener el rolId
        }

        // Actualizar el usuario en la base de datos
        try (Connection conexion = conectarDB();
             PreparedStatement stmt = conexion.prepareStatement(
                     "UPDATE Usuarios SET nombre_usuario = ?, email = ?, contraseña = ?, rol_id = ? ")) {
            stmt.setString(1, nombreUsuario);
            stmt.setString(2, email);
            stmt.setString(3, contraseña);
            stmt.setInt(4, rolId); // Usar el rolId obtenido
            //stmt.setInt(5, id); // Quitar el id del usuario a actualizar

            int filasActualizadas = stmt.executeUpdate();
            if (filasActualizadas > 0) {
                JOptionPane.showMessageDialog(this, "Usuario actualizado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                // Refresca la tabla después de guardar
                cargarTablaEEAG(); // O cualquier método que cargue la tabla de usuarios
                // ... (Limpiar los campos de texto si es necesario)
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo actualizar el usuario.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar usuario: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Desactiva el modo de edición
        modoEdicion = false; 
    } else {
        JOptionPane.showMessageDialog(this, "No estás en modo de edición.", "Información", JOptionPane.INFORMATION_MESSAGE);
    }


   
    }//GEN-LAST:event_jButton_guardar1ActionPerformed

    private void jButton_eliminar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_eliminar1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton_eliminar1ActionPerformed

    private void jTextField_RazonSocialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_RazonSocialActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_RazonSocialActionPerformed

    private void jTextField_NomProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_NomProveedorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_NomProveedorActionPerformed

    private void jTextField_FRegistroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_FRegistroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_FRegistroActionPerformed

    private void jTextField_RUCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_RUCActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_RUCActionPerformed

    private void jTextField_ContactoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_ContactoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_ContactoActionPerformed

    private void jTextField_idProducActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_idProducActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_idProducActionPerformed

    private void jTextField_Buscodigo1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_Buscodigo1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_Buscodigo1ActionPerformed

    private void jButton_buscodigo1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_buscodigo1ActionPerformed
        
        try {
            // 1. Obtener el código ingresado por el usuario
            String codigoBuscado = jTextField_Buscodigo1.getText();

            // 2. Validar si el código está vacío
            if (codigoBuscado.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Por favor, ingrese un código.");
                return; // Detener la ejecución si el código está vacío
            }

            // 2. Establecer la conexión con la base de datos
            String SUrl = "jdbc:mysql://localhost:3306/opticagestioncitas";
            String SUser = "root";
            String SPass = "";
            Connection conexion = DriverManager.getConnection(SUrl, SUser, SPass);

            // 3. Construir la consulta SQL (usando LIKE para búsqueda de patrones)
            String consultaSQL = "SELECT * FROM Productos WHERE codigo LIKE '%" + codigoBuscado + "%'"; 

            // 4. Preparar la sentencia SELECT
            PreparedStatement stmtBuscarProducto = conexion.prepareStatement(consultaSQL);

            // 5. Ejecutar la sentencia para obtener los datos
            ResultSet rs = stmtBuscarProducto.executeQuery();

            // 6. Mostrar los datos en la tabla jTable_buscarpro
            // Crear un modelo de tabla
            DefaultTableModel modeloTabla = new DefaultTableModel();
            // Agregar las columnas deseadas
            modeloTabla.addColumn("ID");
            modeloTabla.addColumn("Código");
            modeloTabla.addColumn("Nombre Producto");
            modeloTabla.addColumn("Cantidad");
            modeloTabla.addColumn("Marca");
            modeloTabla.addColumn("Color");
            modeloTabla.addColumn("Fecha Registro");
            modeloTabla.addColumn("Precio Compra");
            modeloTabla.addColumn("Precio Venta");

            // Asignar el modelo de tabla al JTable 
            jTable_buscarpro.setModel(modeloTabla); 

            // Verificar si se encontró algún resultado
            while (rs.next()) { // Usamos while para agregar todos los resultados
                // Agregar el primer resultado a la tabla
                Object[] fila = new Object[9]; // 9 columnas
                fila[0] = rs.getInt("ID");
                fila[1] = rs.getString("codigo"); 
                fila[2] = rs.getString("nombre_producto"); 
                fila[3] = rs.getInt("cantidad");
                fila[4] = rs.getString("marca");
                fila[5] = rs.getString("color");
                fila[6] = rs.getString("fecha_Registro"); 
                fila[7] = rs.getDouble("precio_com");
                fila[8] = rs.getDouble("precio_ven");

                modeloTabla.addRow(fila);

                // Asignar el modelo de tabla al JTable (Solo una vez)
                jTable_buscarpro.setModel(modeloTabla); 
            } 

            // 7. Cerrar la conexión y la sentencia
            stmtBuscarProducto.close();
            conexion.close();

        } catch (SQLException e) {
            // 8. Manejar excepciones: Imprime el error o muestra un mensaje al usuario
            System.err.println("Error al buscar el producto: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error al buscar el producto: " + e.getMessage());
        } catch (NumberFormatException e) {
            // Manejar la excepción si el ID no es un número válido
            JOptionPane.showMessageDialog(null, "Por favor, ingrese un ID válido.");
        }  
        
        
    }//GEN-LAST:event_jButton_buscodigo1ActionPerformed

    private void jTextField_IdPaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_IdPaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_IdPaActionPerformed

    private void jButton_GuardProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_GuardProveedorActionPerformed
        // TODO add your handling code here:
        try {
            // 1. Obtener los datos del proveedor de los campos de texto
            String nombreProveedor = jTextField_NomProveedor.getText();
            String telefono = jTextField_TelProveedor.getText();
            String email = jTextField_EmailProveedor.getText();
            String direccion = jTextField_DirProveedor.getText();
            String razonSocial = jTextField_RazonSocial.getText();
            String ruc = jTextField_RUC.getText();
            String fRegistro = jTextField_FRegistro.getText(); 
            String contacto = jTextField_Contacto.getText();

            // 2. Verificar si todos los campos están llenos
            if (nombreProveedor.isEmpty() || telefono.isEmpty() || email.isEmpty() || direccion.isEmpty() || razonSocial.isEmpty() || ruc.isEmpty() || fRegistro.isEmpty() || contacto.isEmpty()) {
                // Si alguno de los campos está vacío, mostrar un mensaje de error
                JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos.");
                return; // Detener la ejecución del método
            }

            // 3. Establecer la conexión con la base de datos
            String SUrl = "jdbc:mysql://localhost:3306/opticagestioncitas"; 
            String SUser = "root"; 
            String SPass = ""; 
            Connection conexion = DriverManager.getConnection(SUrl, SUser, SPass);

            // 4. Preparar la sentencia INSERT para agregar el nuevo proveedor
            PreparedStatement stmtInsertar = conexion.prepareStatement(
                "INSERT INTO `proveedores` (`nombre_proveedor`, `telefono`, `email`, `direccion`, `Razon_Social`, `RUC`, `f_registro`, `contacto`) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)");

            // 5. Asignar los valores a la sentencia INSERT
            stmtInsertar.setString(1, nombreProveedor);
            stmtInsertar.setString(2, telefono);
            stmtInsertar.setString(3, email);
            stmtInsertar.setString(4, direccion);
            stmtInsertar.setString(5, razonSocial);
            stmtInsertar.setString(6, ruc);
            stmtInsertar.setString(7, fRegistro);
            stmtInsertar.setString(8, contacto);

            // 6. Ejecutar la sentencia INSERT
            int filasAfectadas = stmtInsertar.executeUpdate();

            // 7. Verificar si se insertó el proveedor correctamente
            if (filasAfectadas > 0) {
                // Si se insertó correctamente, muestra un mensaje al usuario
                JOptionPane.showMessageDialog(null, "¡Proveedor agregado exitosamente!");

                // Limpiar los campos de texto
                jTextField_NomProveedor.setText("");
                jTextField_TelProveedor.setText("");
                jTextField_EmailProveedor.setText("");
                jTextField_DirProveedor.setText("");
                jTextField_RazonSocial.setText("");
                jTextField_RUC.setText("");
                jTextField_FRegistro.setText("");
                jTextField_Contacto.setText("");
            } else {
                // Si no se insertó, muestra un mensaje de error
                JOptionPane.showMessageDialog(null, "Error al agregar el proveedor.");
            }

            // 8. Cerrar la conexión y la sentencia
            stmtInsertar.close();
            conexion.close();

        } catch (SQLException e) {
            // Manejar excepciones:  Imprime el error o muestra un mensaje al usuario
            System.err.println("Error al agregar el proveedor: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error al agregar el proveedor: " + e.getMessage());
        }
    }//GEN-LAST:event_jButton_GuardProveedorActionPerformed

    private void jTextField_IDProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_IDProveedorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_IDProveedorActionPerformed

    private void jButton_ProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ProveedorActionPerformed
        // TODO add your handling code here:
        try {
        // 1. Obtener el texto ingresado por el usuario
        String busqueda = jTextField_IDProveedor.getText(); 

        // 2. Validar si el campo está vacío
        if (busqueda.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, ingrese un ID o nombre.");
            return; // Detener la ejecución si el campo está vacío
        }

        // 2. Establecer la conexión con la base de datos
        String SUrl = "jdbc:mysql://localhost:3306/opticagestioncitas";
        String SUser = "root";
        String SPass = "";
        Connection conexion = DriverManager.getConnection(SUrl, SUser, SPass);

        // 3. Construir la consulta SQL (usando un OR para buscar por ID o por nombre)
        String consultaSQL = "SELECT * FROM proveedores WHERE ID = ? OR nombre_proveedor LIKE ?"; // Usar un signo de interrogación (?) como marcador de posición

        // 4. Preparar la sentencia SELECT
        PreparedStatement stmtBuscarProveedor = conexion.prepareStatement(consultaSQL);

        // 5. Asignar los valores a la sentencia preparada
        try {
            // Intenta convertir el valor a un entero (ID)
            int id = Integer.parseInt(busqueda);
            stmtBuscarProveedor.setInt(1, id);
            stmtBuscarProveedor.setString(2, "%"); // Usar % como comodín para el nombre
        } catch (NumberFormatException e) {
            // Si no se puede convertir a entero, es un nombre
            stmtBuscarProveedor.setInt(1, 0); // Usar 0 como valor ficticio para el ID
            stmtBuscarProveedor.setString(2, "%" + busqueda + "%"); // Buscar por nombre
        }

        // 6. Ejecutar la sentencia para obtener los datos
        ResultSet rs = stmtBuscarProveedor.executeQuery();

        // 7. Mostrar los datos en la tabla jTable_proveedor
        // Crear un modelo de tabla
        DefaultTableModel modeloTabla = new DefaultTableModel();
        // Agregar las columnas deseadas
        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Nombre Proveedor");
        modeloTabla.addColumn("Teléfono Proveedor");
        modeloTabla.addColumn("Correo Proveedor");
        modeloTabla.addColumn("Dirección Proveedor");
        modeloTabla.addColumn("Razón Social");
        modeloTabla.addColumn("RUC");
        modeloTabla.addColumn("Fecha Registro");
        modeloTabla.addColumn("Contacto");

        // Asignar el modelo de tabla al JTable 
        jTable_proveedor.setModel(modeloTabla); 

        // Verificar si se encontró algún resultado
        while (rs.next()) { 
            // Agregar el resultado a la tabla
            Object[] fila = new Object[9]; // 9 columnas (sin "codigo")
            fila[0] = rs.getInt("ID");
            fila[1] = rs.getString("nombre_proveedor"); 
            fila[2] = rs.getString("telefono");
            fila[3] = rs.getString("email");
            fila[4] = rs.getString("direccion");
            fila[5] = rs.getString("Razon_Social");
            fila[6] = rs.getString("RUC");
            fila[7] = rs.getString("f_registro"); 
            fila[8] = rs.getString("contacto");

            modeloTabla.addRow(fila);

            // Asignar el modelo de tabla al JTable (Solo una vez)
            jTable_proveedor.setModel(modeloTabla); 
        } 

        // 8. Cerrar la conexión y la sentencia
        stmtBuscarProveedor.close();
        conexion.close();

    } catch (SQLException e) {
        // Manejar excepciones:  Imprime el error o muestra un mensaje al usuario
        System.err.println("Error al buscar el proveedor: " + e.getMessage());
        JOptionPane.showMessageDialog(null, "Error al buscar el proveedor: " + e.getMessage());
    } catch (NumberFormatException e) {
        // Manejar la excepción si el ID no es un número válido
        // En este caso, asumimos que es un nombre
        // Puedes mostrar un mensaje de error o simplemente continuar la búsqueda
        JOptionPane.showMessageDialog(null, "Por favor, ingrese un ID o nombre válido.");
    }
        

        // Ajustar el tamaño de las columnas automáticamente
        jTable_proveedor.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // Establecer ancho preferido para las columnas (corregido)
        jTable_proveedor.getColumnModel().getColumn(0).setPreferredWidth(20); // Columna ID
        jTable_proveedor.getColumnModel().getColumn(1).setPreferredWidth(110); // Columna Nombre Proveedor
        jTable_proveedor.getColumnModel().getColumn(2).setPreferredWidth(110); // Columna Dirección Proveedor
        jTable_proveedor.getColumnModel().getColumn(3).setPreferredWidth(110); // Columna Teléfono Proveedor
        jTable_proveedor.getColumnModel().getColumn(4).setPreferredWidth(110); // Columna Email Proveedor
        jTable_proveedor.getColumnModel().getColumn(5).setPreferredWidth(110); // Columna Razón Social
        jTable_proveedor.getColumnModel().getColumn(6).setPreferredWidth(80); // Columna RUC
        jTable_proveedor.getColumnModel().getColumn(7).setPreferredWidth(100); // Columna Fecha Registro
        jTable_proveedor.getColumnModel().getColumn(8).setPreferredWidth(80); // Columna Contacto 

    }//GEN-LAST:event_jButton_ProveedorActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton_GmostrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_GmostrarActionPerformed
        // TODO add your handling code here:
        try {
            // 1. Establecer la conexión con la base de datos
            String SUrl = "jdbc:mysql://localhost:3306/opticagestioncitas"; 
            String SUser = "root"; 
            String SPass = ""; 
            Connection conexion = DriverManager.getConnection(SUrl, SUser, SPass);

            // 2. Preparar la sentencia SELECT para Productos
            PreparedStatement stmtMostrarProductos = conexion.prepareStatement(
                "SELECT ID, codigo, nombre_producto, cantidad, marca, color, fecha_Registro, precio_com, precio_ven FROM Productos"); 

            // 3. Ejecutar la sentencia para obtener los datos de Productos
            ResultSet rsProductos = stmtMostrarProductos.executeQuery();

            // 4. Mostrar los datos en un JTable (o en otro componente visual)
            // Crear un modelo de tabla
            DefaultTableModel modeloTabla = new DefaultTableModel();
            // Agregar las columnas deseadas
            modeloTabla.addColumn("ID");
            modeloTabla.addColumn("Código");
            modeloTabla.addColumn("Nombre Producto");
            modeloTabla.addColumn("Cantidad");
            modeloTabla.addColumn("Marca");
            modeloTabla.addColumn("Color");
            modeloTabla.addColumn("Fecha Registro");
            modeloTabla.addColumn("Precio Compra");
            modeloTabla.addColumn("Precio Venta");

            // Iterar sobre los resultados de Productos
            while (rsProductos.next()) {
                Object[] fila = new Object[9]; // 9 columnas
                fila[0] = rsProductos.getInt("ID");
                fila[1] = rsProductos.getString("codigo"); 
                fila[2] = rsProductos.getString("nombre_producto"); // Corregido para usar "nom_producto"
                fila[3] = rsProductos.getInt("cantidad");
                fila[4] = rsProductos.getString("marca");
                fila[5] = rsProductos.getString("color");
                fila[6] = rsProductos.getString("fecha_Registro"); 
                fila[7] = rsProductos.getDouble("precio_com");
                fila[8] = rsProductos.getDouble("precio_ven");

                modeloTabla.addRow(fila);
            }
            // Asigna el modelo de tabla al JTable 
            jTable_MostrarProductos.setModel(modeloTabla); // Corregido: jTable_MostrarProductos

            // Ajustar el tamaño de las columnas automáticamente
            jTable_MostrarProductos.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

            // Ajustar el tamaño de las columnas automáticamente
            jTable_MostrarProductos.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

            // Establecer ancho preferido para las demás columnas
            jTable_MostrarProductos.getColumnModel().getColumn(0).setPreferredWidth(50); // Columna ID
            jTable_MostrarProductos.getColumnModel().getColumn(1).setPreferredWidth(100); // Columna Código
            jTable_MostrarProductos.getColumnModel().getColumn(2).setPreferredWidth(150); // Columna Nombre Producto
            jTable_MostrarProductos.getColumnModel().getColumn(3).setPreferredWidth(70); // Columna Cantidad
            jTable_MostrarProductos.getColumnModel().getColumn(4).setPreferredWidth(100); // Columna Marca
            jTable_MostrarProductos.getColumnModel().getColumn(5).setPreferredWidth(80); // Columna Color
            jTable_MostrarProductos.getColumnModel().getColumn(6).setPreferredWidth(120); // Columna Fecha Registro
            jTable_MostrarProductos.getColumnModel().getColumn(7).setPreferredWidth(100); // Columna Precio Compra
            jTable_MostrarProductos.getColumnModel().getColumn(8).setPreferredWidth(100); // Columna Precio Venta




            // 7. Cerrar la conexión y la sentencia
            stmtMostrarProductos.close();
            conexion.close();

        } catch (SQLException e) {
            // Manejar excepciones:  Imprime el error o muestra un mensaje al usuario
            System.err.println("Error al mostrar los productos: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error al mostrar los productos: " + e.getMessage());
        }  
    
    }//GEN-LAST:event_jButton_GmostrarActionPerformed
    // Método para obtener el ID del rol a partir del nombre del rol
    private int getRoleId(String roleName) {
        switch (roleName) {
            case "Administrador":
                return 1; // Asume que el ID del rol de Administrador es 1        
            case "Optometrista":
                return 2; // Asume que el ID del rol de Optometrista es 2
            case "Recepcionista":
                return 3; // Asume que el ID del rol de Recepcionista es 3
            default:
                return 3; // Valor predeterminado en caso de que no se encuentre el rol
            }
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Dashboard_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Dashboard_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Dashboard_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Dashboard_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                
                new Dashboard_Form().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField AP;
    private javax.swing.JTextField Ape;
    private javax.swing.JTextField CE;
    private javax.swing.JTextField DC;
    private javax.swing.JTextField Dire;
    private javax.swing.JTextField Edad;
    private javax.swing.JTextField Ema;
    private javax.swing.JTextField FN;
    private javax.swing.JTextField Hora;
    private javax.swing.JTextField ID;
    private javax.swing.JTextField ID_user;
    private javax.swing.JButton LogoutBtn;
    private javax.swing.JTextField NOP;
    private javax.swing.JTextField NP;
    private javax.swing.JTextField Nom;
    private javax.swing.JComboBox<String> Roles1;
    private javax.swing.JTextField Sex;
    private javax.swing.JButton SignUpBtn2;
    private javax.swing.JTextField TL;
    private javax.swing.JTextField Tele;
    private javax.swing.JTextField emailAddress1;
    private javax.swing.JTextField fname1;
    private javax.swing.JButton jButton_BusHistoria;
    private javax.swing.JButton jButton_Gmostrar;
    private javax.swing.JButton jButton_GuardProveedor;
    private javax.swing.JButton jButton_ID;
    private javax.swing.JButton jButton_Id_11;
    private javax.swing.JButton jButton_LimpiarBusqueda;
    private javax.swing.JButton jButton_MostrarPacientes;
    private javax.swing.JButton jButton_MostrarUsuarios;
    private javax.swing.JButton jButton_Proveedor;
    private javax.swing.JButton jButton_RHC;
    private javax.swing.JButton jButton_Rcita;
    private javax.swing.JButton jButton_RegisProducto;
    private javax.swing.JButton jButton_buscar;
    private javax.swing.JButton jButton_buscodigo1;
    private javax.swing.JButton jButton_edit;
    private javax.swing.JButton jButton_editar;
    private javax.swing.JButton jButton_editar1;
    private javax.swing.JButton jButton_elimi;
    private javax.swing.JButton jButton_eliminar;
    private javax.swing.JButton jButton_eliminar1;
    private javax.swing.JButton jButton_guar;
    private javax.swing.JButton jButton_guardar;
    private javax.swing.JButton jButton_guardar1;
    private javax.swing.JButton jButton_mostrar;
    private javax.swing.JButton jButton_registrar;
    private javax.swing.JComboBox<String> jComboBox_Sexo;
    private javax.swing.JComboBox<String> jComboBox_pago;
    private javax.swing.JComboBox<String> jCombo_Servicio;
    private com.toedter.calendar.JDateChooser jDateChooser_Cita;
    private com.toedter.calendar.JDateChooser jDateChooser_Nacimiento;
    private javax.swing.JInternalFrame jInternalFrame1;
    private javax.swing.JInternalFrame jInternalFrame2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel_AP;
    private javax.swing.JLabel jLabel_Ape;
    private javax.swing.JLabel jLabel_Ape1;
    private javax.swing.JLabel jLabel_BC1;
    private javax.swing.JLabel jLabel_CE;
    private javax.swing.JLabel jLabel_Dir;
    private javax.swing.JLabel jLabel_Direc;
    private javax.swing.JLabel jLabel_Direc1;
    private javax.swing.JLabel jLabel_Direc2;
    private javax.swing.JLabel jLabel_Direc3;
    private javax.swing.JLabel jLabel_Ema;
    private javax.swing.JLabel jLabel_Ema1;
    private javax.swing.JLabel jLabel_FNa;
    private javax.swing.JLabel jLabel_FNa1;
    private javax.swing.JLabel jLabel_Fcita;
    private javax.swing.JLabel jLabel_H;
    private javax.swing.JLabel jLabel_H1;
    private javax.swing.JLabel jLabel_ID;
    private javax.swing.JLabel jLabel_IDservicio;
    private javax.swing.JLabel jLabel_IDservicio1;
    private javax.swing.JLabel jLabel_Id1;
    private javax.swing.JLabel jLabel_Id_1;
    private javax.swing.JLabel jLabel_MP;
    private javax.swing.JLabel jLabel_MPago;
    private javax.swing.JLabel jLabel_MPago1;
    private javax.swing.JLabel jLabel_NP;
    private javax.swing.JLabel jLabel_Nom;
    private javax.swing.JLabel jLabel_Nom1;
    private javax.swing.JLabel jLabel_Pac;
    private javax.swing.JLabel jLabel_Prec;
    private javax.swing.JLabel jLabel_PrecioV;
    private javax.swing.JLabel jLabel_Rol;
    private javax.swing.JLabel jLabel_Rol1;
    private javax.swing.JLabel jLabel_TL;
    private javax.swing.JLabel jLabel_Tele;
    private javax.swing.JLabel jLabel_Tele1;
    private javax.swing.JLabel jLabel_appLogo;
    private javax.swing.JLabel jLabel_ca;
    private javax.swing.JLabel jLabel_cd1;
    private javax.swing.JLabel jLabel_cd2;
    private javax.swing.JLabel jLabel_cd3;
    private javax.swing.JLabel jLabel_colorp;
    private javax.swing.JLabel jLabel_eda;
    private javax.swing.JLabel jLabel_eda1;
    private javax.swing.JLabel jLabel_fecha;
    private javax.swing.JLabel jLabel_fregis;
    private javax.swing.JLabel jLabel_menuItem1;
    private javax.swing.JLabel jLabel_menuItem2;
    private javax.swing.JLabel jLabel_menuItem3;
    private javax.swing.JLabel jLabel_menuItem4;
    private javax.swing.JLabel jLabel_menuItem5;
    private javax.swing.JLabel jLabel_menuItem6;
    private javax.swing.JLabel jLabel_menuItem7;
    private javax.swing.JLabel jLabel_menuItem8;
    private javax.swing.JLabel jLabel_menuItem9;
    private javax.swing.JLabel jLabel_motivo;
    private javax.swing.JLabel jLabel_motivo12;
    private javax.swing.JLabel jLabel_produc;
    private javax.swing.JLabel jLabel_s;
    private javax.swing.JLabel jLabel_sex;
    private javax.swing.JLabel jLabel_sex1;
    private javax.swing.JLabel jLabel_us;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel_BCPRO;
    private javax.swing.JPanel jPanel_BCPROVE;
    private javax.swing.JPanel jPanel_BSCP;
    private javax.swing.JPanel jPanel_CPS;
    private javax.swing.JPanel jPanel_Citas1;
    private javax.swing.JPanel jPanel_Con;
    private javax.swing.JPanel jPanel_Con1;
    private javax.swing.JPanel jPanel_GI;
    private javax.swing.JPanel jPanel_GRI;
    private javax.swing.JPanel jPanel_GRV;
    private javax.swing.JPanel jPanel_GRVEN;
    private javax.swing.JPanel jPanel_LV;
    private javax.swing.JPanel jPanel_RGP;
    private javax.swing.JPanel jPanel_RNUS;
    private javax.swing.JPanel jPanel_RPRO;
    private javax.swing.JPanel jPanel_VE;
    private javax.swing.JPanel jPanel_ayuda;
    private javax.swing.JPanel jPanel_citas;
    private javax.swing.JPanel jPanel_container;
    private javax.swing.JPanel jPanel_dashboard;
    private javax.swing.JPanel jPanel_historias1;
    private javax.swing.JPanel jPanel_logoANDname;
    private javax.swing.JPanel jPanel_menu;
    private javax.swing.JPanel jPanel_pacientes;
    private javax.swing.JPanel jPanel_productos;
    private javax.swing.JPanel jPanel_proveedores;
    private javax.swing.JPanel jPanel_reportes;
    private javax.swing.JPanel jPanel_usuarios;
    private javax.swing.JPanel jPanel_ventas;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane10;
    private javax.swing.JTabbedPane jTabbedPane11;
    private javax.swing.JTabbedPane jTabbedPane5;
    private javax.swing.JTabbedPane jTabbedPane6;
    private javax.swing.JTabbedPane jTabbedPane7;
    private javax.swing.JTabbedPane jTabbedPane8;
    private javax.swing.JTabbedPane jTabbedPane9;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable_BSC;
    private javax.swing.JTable jTable_EEAG;
    private javax.swing.JTable jTable_Historias;
    private javax.swing.JTable jTable_MostrarProductos;
    private javax.swing.JTable jTable_Productos;
    private javax.swing.JTable jTable_buscarpro;
    private javax.swing.JTable jTable_proveedor;
    private javax.swing.JTextArea jTextArea_Diag14;
    private javax.swing.JTextArea jTextArea_Obse;
    private javax.swing.JTextArea jTextArea_Obse12;
    private javax.swing.JTextArea jTextArea_Trata13;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField_Ape1;
    private javax.swing.JTextField jTextField_Buscodigo1;
    private javax.swing.JTextField jTextField_Contacto;
    private javax.swing.JTextField jTextField_DirProveedor;
    private javax.swing.JTextField jTextField_Dire1;
    private javax.swing.JTextField jTextField_E;
    private javax.swing.JTextField jTextField_EmailProveedor;
    private javax.swing.JTextField jTextField_FN1;
    private javax.swing.JTextField jTextField_FRegistro;
    private javax.swing.JTextField jTextField_Hora1;
    private javax.swing.JTextField jTextField_IDProveedor;
    private javax.swing.JTextField jTextField_IDServicio;
    private javax.swing.JTextField jTextField_Id11;
    private javax.swing.JTextField jTextField_IdA;
    private javax.swing.JTextField jTextField_IdPa;
    private javax.swing.JTextField jTextField_Nom1;
    private javax.swing.JTextField jTextField_NomProveedor;
    private javax.swing.JTextField jTextField_RC;
    private javax.swing.JTextField jTextField_RUC;
    private javax.swing.JTextField jTextField_RazonSocial;
    private javax.swing.JTextField jTextField_S;
    private javax.swing.JTextField jTextField_TelProveedor;
    private javax.swing.JTextField jTextField_Tf;
    private javax.swing.JTextField jTextField_cantidad;
    private javax.swing.JTextField jTextField_codigo;
    private javax.swing.JTextField jTextField_color;
    private javax.swing.JTextField jTextField_compra;
    private javax.swing.JTextField jTextField_em;
    private javax.swing.JTextField jTextField_idProduc;
    private javax.swing.JTextField jTextField_marca;
    private javax.swing.JTextField jTextField_precio;
    private javax.swing.JTextField jTextField_producto;
    private javax.swing.JTextField jTextField_registro;
    private javax.swing.JTextField jTextField_venta;
    private javax.swing.JPasswordField pass2;
    private javax.swing.JLabel user;
    // End of variables declaration//GEN-END:variables

}