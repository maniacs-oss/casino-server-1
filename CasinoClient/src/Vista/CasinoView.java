/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Casino.java
 *
 * Created on 29-dic-2011, 14:00:25
 */
package Vista;

import controlador.ControladorCartaAlta;
import controlador.ControladorDados;
import controlador.ControladorPoker;
import java.awt.CardLayout;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;
import juego.Dados.ModeloDados;
import juego.Poker.ModeloPoker;
import general.Casino;

/**
 *
 * @author Miguel Angel Fuentes Cardenas
 */
public class CasinoView extends javax.swing.JFrame implements Observer {
    
    private int idioma;
    private Casino casino;
    private ControladorCartaAlta controller;
    private IdiomaView idiomaVista;
    ModeloPoker model_pok;
    InterfazPoker vistaPoker;
    ModeloDados model_dados; 
    InterfazDados view_dados;
    JugadorRuleta vistaRuleta;
    PanelCartaAlta vistaCartaAlta;
    
    public IdiomaView getIdiomaVista() {
        return idiomaVista;
    }

    /** Creates new form Casino */
    public CasinoView(Casino casino, ControladorCartaAlta controller) {
        this.casino = casino;
        this.controller = controller;
        
        model_pok = new ModeloPoker();
        vistaPoker = new InterfazPoker(model_pok);
        model_dados = new ModeloDados();
        view_dados = new InterfazDados(model_dados);
        vistaRuleta=new JugadorRuleta(casino);
        vistaCartaAlta=new PanelCartaAlta(casino, controller);
        //INCIALIAZO LOS COMPONENTES
        initComponents();


        //CENTRO LA VENTANA EN PANTALLA
        this.setLocationRelativeTo(null);

        //OBTENGO IDIOMA POR DEFECTO
        InputStream f;
        Properties defProps = new Properties();
        idioma = 0;
        try {            
            f = new FileInputStream("Recursos/config.properties");
            defProps.load(f);
            idioma = Integer.parseInt(defProps.getProperty("default_languaje"));
            f.close();            
        } catch (IOException e) {
            System.err.println("Error al leer de config.properties");
        }

        //CARGO LA VISTA CON EL IDIOMA POR DEFECTO
        this.CargarIdioma();

        //AGREGO PANELES
        CargarPaneles();

        //AGREGO A ESTA CLASE COMO OBSERVADORA DE CASINO
        this.casino.addObserver(this);
    }
    
    public void showPanel(String panel) {
        CardLayout cl = (CardLayout) (PanelPrincipal.getLayout());
        cl.next(PanelPrincipal);
        cl.show(PanelPrincipal, panel);
    }
   
    private void CargarPaneles() {
        this.PanelPrincipal.add(new PanelLogin(idioma, controller, this), "login");
        this.PanelPrincipal.add(new PanelSelectGame(idioma, controller, casino, this), "selectgame");
        
        this.PanelPrincipal.add(vistaCartaAlta, "carta_alta");

        //POKER
        ControladorPoker control = new ControladorPoker(model_pok, vistaPoker, casino);
        this.PanelPrincipal.add(vistaPoker, "poker");


        //DADOS
        ControladorDados control_dados = new ControladorDados(model_dados, view_dados, casino);
        this.PanelPrincipal.add(view_dados, "dados");


        //RULETA
        this.PanelPrincipal.add(vistaRuleta, "ruleta");
        
    }
    
    private void CargarIdioma() {
        String fichero = "";
        InputStream f;
        Properties defProps = new Properties();
        
        switch (idioma) {            
            case 0: //ESPANOL
                fichero = "Recursos/lenguajes_es_ES.properties";
                break;
            
            case 1: //INGLES
                fichero = "Recursos/lenguajes_en_GB.properties";
                break;
        }


        //CONFIGURO LA INTERFAZ EN FUNCION DEL IDIOMA
        try {            
            f = new FileInputStream(fichero);
            defProps.load(f);
            
            this.info.setText(defProps.getProperty("info"));            
            this.MenuConfiguracion.setText(defProps.getProperty("preferences"));
            this.MenuConexion.setText(defProps.getProperty("connection"));
            this.MenuIdioma.setText(defProps.getProperty("languaje"));
            this.MenuAcercade.setText(defProps.getProperty("about"));
            //Modifico los labels de la interfaz del poker
            vistaPoker.getBtnConectar().setText(defProps.getProperty("Comenzar"));
            vistaPoker.getBtnApuesto().setText(defProps.getProperty("Apuesto"));
            vistaPoker.getBtnPaso().setText(defProps.getProperty("Paso"));
            vistaPoker.getBtnResto().setText(defProps.getProperty("Resto"));
            vistaPoker.getLblCantidadAApostar().setText(defProps.getProperty("Apostar"));
            vistaPoker.getLblPuntos().setText(defProps.getProperty("PuntosAcumulados"));
            vistaPoker.getLblTotalEnLa().setText(defProps.getProperty("TotalMesa"));
            //Modifico los labels de la interfaz de los dados
            view_dados.getjLabel1().setText(defProps.getProperty("Saldo"));
            view_dados.getjButton3_apostar().setText(defProps.getProperty("Apostar"));
            view_dados.getDlg().getjLabel1().setText(defProps.getProperty("IntroduzcaCantidadApostar"));
            view_dados.getDlg().getjLabel2().setText(defProps.getProperty("ElijaApuestaRealizar"));
            view_dados.getDlg().getjButton2().setText(defProps.getProperty("Aceptar")); 
            //Modifico los labels de la interfaz de la ruleta
            vistaRuleta.getjLabel1().setText(defProps.getProperty("SaldoDisponible"));
            vistaRuleta.getjLabel2().setText(defProps.getProperty("CantidadApuesta"));
            vistaRuleta.getjButton37().setText(defProps.getProperty("Par"));
            vistaRuleta.getjButton38().setText(defProps.getProperty("Impar"));
            vistaRuleta.getjButton39().setText(defProps.getProperty("Rojo"));
            vistaRuleta.getjButton40().setText(defProps.getProperty("Negro"));
            vistaRuleta.getjButton41().setText(defProps.getProperty("Bajos"));
            vistaRuleta.getjButton42().setText(defProps.getProperty("Altos"));
            //Modifico los labels de la carta mas alta
            vistaCartaAlta.getLabelApuesta().setText(defProps.getProperty("Apuesta"));
            vistaCartaAlta.getLabelJugador().setText(defProps.getProperty("Jugador"));
            vistaCartaAlta.getLabelPuntos().setText(defProps.getProperty("Puntos"));
            vistaCartaAlta.getBotonJugar().setText(defProps.getProperty("Jugar"));
            vistaCartaAlta.getBotonOtra().setText(defProps.getProperty("OtraVez"));
            
            f.close();
        } catch (IOException e) {
            System.err.println("Error al cargar idioma por defecto");
        }
    }

    //ESTABLECE EL IDIOMA Y ACTUALIZA LA INTERFAZ
    public void setIdioma(int idioma) {
        this.idioma = idioma;
        this.CargarIdioma();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PanelInferior = new javax.swing.JPanel();
        LabelInfo = new javax.swing.JLabel();
        info = new javax.swing.JLabel();
        PanelPrincipal = new javax.swing.JPanel();
        BarraMenu = new javax.swing.JMenuBar();
        MenuConfiguracion = new javax.swing.JMenu();
        MenuConexion = new javax.swing.JMenuItem();
        MenuIdioma = new javax.swing.JMenuItem();
        MenuAcercade = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Casino Online");
        setMinimumSize(new java.awt.Dimension(800, 600));
        setResizable(false);

        PanelInferior.setBackground(new java.awt.Color(255, 255, 255));
        PanelInferior.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));
        PanelInferior.setPreferredSize(new java.awt.Dimension(86, 22));
        PanelInferior.setSize(new java.awt.Dimension(86, 22));

        LabelInfo.setFont(new java.awt.Font("Lucida Grande", 1, 14));
        LabelInfo.setText("Info:");

        info.setFont(new java.awt.Font("Lucida Grande", 0, 14));
        info.setText("jLabel1");

        org.jdesktop.layout.GroupLayout PanelInferiorLayout = new org.jdesktop.layout.GroupLayout(PanelInferior);
        PanelInferior.setLayout(PanelInferiorLayout);
        PanelInferiorLayout.setHorizontalGroup(
            PanelInferiorLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(PanelInferiorLayout.createSequentialGroup()
                .add(20, 20, 20)
                .add(LabelInfo)
                .add(18, 18, 18)
                .add(info, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 563, Short.MAX_VALUE))
        );
        PanelInferiorLayout.setVerticalGroup(
            PanelInferiorLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(PanelInferiorLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(LabelInfo)
                .add(info))
        );

        PanelPrincipal.setLayout(new java.awt.CardLayout());

        MenuConfiguracion.setText("Propiedades");

        MenuConexion.setText("Conexion");
        MenuConexion.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                MenuConexionMouseReleased(evt);
            }
        });
        MenuConfiguracion.add(MenuConexion);

        MenuIdioma.setText("Idioma");
        MenuIdioma.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                MenuIdiomaMouseReleased(evt);
            }
        });
        MenuConfiguracion.add(MenuIdioma);

        MenuAcercade.setText("Acerca de");
        MenuAcercade.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                MenuAcercadeMouseReleased(evt);
            }
        });
        MenuConfiguracion.add(MenuAcercade);

        BarraMenu.add(MenuConfiguracion);

        setJMenuBar(BarraMenu);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(PanelInferior, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 637, Short.MAX_VALUE)
            .add(PanelPrincipal, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 637, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .add(PanelPrincipal, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 391, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(PanelInferior, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        this.CargarPaneles();

        pack();
    }// </editor-fold>//GEN-END:initComponents

public void MenuIdiomaMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MenuIdiomaMouseReleased
    //LANZA EL JDIALOG PARA SELECCION DE IDIOMA
    new IdiomaView(this, rootPaneCheckingEnabled, idioma).setVisible(true);
}//GEN-LAST:event_MenuIdiomaMouseReleased
    
    public void setIdiomaVista(IdiomaView idiomaVista) {
        this.idiomaVista = idiomaVista;
        System.out.println(idiomaVista == null);
//        vistaPoker.getBtnConectar().setText(idiomaVista.getDefProps().getProperty("Comenzar"));
//        vistaPoker.getBtnApuesto().setText(idiomaVista.getDefProps().getProperty("Apuesto"));
//        vistaPoker.getBtnPaso().setText(idiomaVista.getDefProps().getProperty("Paso"));
//        vistaPoker.getBtnResto().setText(idiomaVista.getDefProps().getProperty("Resto"));
//        vistaPoker.getLblCantidadAApostar().setText(idiomaVista.getDefProps().getProperty("Apostar"));
//        vistaPoker.getLblPuntos().setText(idiomaVista.getDefProps().getProperty("PuntosAcumulados"));
//        vistaPoker.getLblTotalEnLa().setText(idiomaVista.getDefProps().getProperty("TotalMesa"));
    }
private void MenuConexionMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MenuConexionMouseReleased
    //LANZA EL JDIALOG DE CONFIGURACION DE CONEXION
    new ConexionView(this, rootPaneCheckingEnabled, idioma).setVisible(true);
}//GEN-LAST:event_MenuConexionMouseReleased
    
private void MenuAcercadeMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MenuAcercadeMouseReleased
    //LANZA EL JDIALOG CON INFORMACION SOBRE LOS CREADORES
    new AboutView(this, rootPaneCheckingEnabled, idioma).setVisible(true);
}//GEN-LAST:event_MenuAcercadeMouseReleased
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuBar BarraMenu;
    private javax.swing.JLabel LabelInfo;
    private javax.swing.JMenuItem MenuAcercade;
    private javax.swing.JMenuItem MenuConexion;
    private javax.swing.JMenu MenuConfiguracion;
    private javax.swing.JMenuItem MenuIdioma;
    private javax.swing.JPanel PanelInferior;
    private javax.swing.JPanel PanelPrincipal;
    private javax.swing.JLabel info;
    // End of variables declaration//GEN-END:variables

    @Override
    public void update(Observable o, Object o1) {
        
        if (o == this.casino) {
            this.info.setText(casino.getMensaje());
        }
        
    }
}
