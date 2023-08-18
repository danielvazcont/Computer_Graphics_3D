/*
José Daniel Vázquez Franco.  
Practica 7: Curva
*/
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.lang.Math;

public class practica7 extends JFrame implements KeyListener {
    private BufferedImage imagePixel;
    private JPanel area;
    private JTextArea textArea;

    private int rangoMax = 0;
    private int curvaminimoX = 0;
    private int curvaminimoY = 0;
    private int curvaminimoZ = 0;

    private BufferedImage buffer;
    private Image fondo;

    //Variable para el grosor del pixel
    private int grosor = 1;

    //Puntos de la curva
    private final int cantidad_puntos = 7000;
    

    //Puntos 3d de la curva
    int curva[][] = new int[cantidad_puntos][4];

    //escalacion
    double[] escalacion = {0.05,0.05,0.05};

    //rotacion
    double[] rotacion = {0,0,1.5};

    //translacion inicial
    int[] traslacion = {-3800, -2950, 0};

    //translacion
    int[] traslacionCurva = {200, 200, 0};
    
    //plano de proyeccion
    int[] paraleloCam = {100,200,100};


    //Constructor
    public practica7(){
        area = new JPanel();
        textArea = new JTextArea();
        textArea.addKeyListener(this);
        getContentPane().add(textArea, BorderLayout.CENTER);
        getContentPane().add(area, BorderLayout.CENTER);

        
        setTitle("Practica 7_Curva");
        setResizable(true);
		setSize(800,600);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        fondo = area.createImage(area.getWidth(), area.getHeight());
        imagePixel = new BufferedImage(grosor, grosor, BufferedImage.TYPE_INT_RGB);

        //no lo dibuja a la primera
        pantalla();
        pantalla();
    }
    
    //funcion para la curva
    public void curva_parametrica(int curva[][]){
    
        double tempX, tempY, tempZ;
        double maximoX = 0, minimoX = 0, maximoY = 0, minimoY = 0, maximoZ = 0, minimoZ = 0;

        //de 
        double t = 0;
        //hasta
        double incr = 2 * Math.PI / cantidad_puntos;

        for (int i = 0; i < cantidad_puntos; i++) {
            //ecuaciones parametricas
            tempX = (1 * Math.cos(t)) * 3300;
            tempY = (1 * Math.sin(t)) * 3300;
            tempZ = (0.3 * Math.cos(10* t)) * 3300;
            
            t = incr * i;

            curva[i][0] = (int)Math.round(tempX);
            curva[i][1] = (int)Math.round(tempY);
            curva[i][2] = (int)Math.round(tempZ); 
            curva[i][3] = 1;

            if(i == 0){
                maximoZ = tempZ;
                minimoZ = tempZ;
            }

            if (tempX < minimoX){
                minimoX = tempX;
            }
            if (tempX > maximoX){
                maximoX = tempX;
            }

            if (tempY < minimoY){
                minimoY = tempY;
            }
            if (tempY > maximoY){
                maximoY = tempY;
            }

            if (tempZ < minimoZ){
                minimoZ = tempZ;
            }
            if (tempZ > maximoZ){
                maximoZ = tempZ;
            }
        }
        double rangoX = maximoX-minimoX;
        double rangoY = maximoY-minimoY;
        double rangoZ = maximoZ-minimoZ;

        curvaminimoX = (int)Math.round(minimoX);
        curvaminimoX = (int)Math.round(minimoY);
        curvaminimoX = (int)Math.round(minimoZ);

        if((rangoX) > (rangoY)){
            if((rangoX) > (rangoZ)){
                rangoMax = (int)Math.round(rangoX);
            }
            else{
                rangoMax = (int)Math.round(rangoZ);
            }
        }
        else if((rangoY) > (rangoZ)){
            rangoMax = (int)Math.round(rangoY);
        }
        else{
            rangoMax = (int)Math.round(rangoZ);
        }

        traslacionCurva[0] = rangoMax;
        traslacionCurva[1] = rangoMax;
        paraleloCam[2] = rangoMax;
    }

    //Dibujar curva
    public void dibujarCurva(int curva[][], int cam[], Color c){
        int curva2D[][] = new int [cantidad_puntos][2];

        for (int i = 0; i < cantidad_puntos; i++) {
            curva2D[i] = paralelaProy(curva[i][0], curva[i][1], curva[i][2], cam[0], cam[1], cam[2]);
        }

        for (int i = 1; i < cantidad_puntos; i++) {
            linea(curva2D[i-1][0], curva2D[i-1][1], curva2D[i][0], curva2D[i][1], c);
        }
    }


    //Dibujar
    public void pantalla(){
        buffer = new BufferedImage(area.getWidth(), area.getHeight(), BufferedImage.TYPE_INT_RGB);
        buffer.getGraphics().drawImage(fondo, 0, 0, this);
        curva_parametrica(curva);
       
        
        rotar(curva, rotacion[0], rotacion[1], rotacion[2]);
        escalar(curva, escalacion[0], escalacion[1], escalacion[2]);
        trasladar(curva, -rangoMax/2, -rangoMax/2, -rangoMax/2);
        trasladar(curva, -curvaminimoX, -curvaminimoY, -curvaminimoZ);
        trasladar(curva, traslacionCurva[0], traslacionCurva[1], traslacionCurva[2]);
        trasladar(curva, traslacion[0], traslacion[1], traslacion[2]);
        
        //Dibujar Función
        
        dibujarCurva(curva, paraleloCam, Color.black);

        area.getGraphics().drawImage(buffer, 0, 0, this);
    }

    

    public static void main(String[] args) {
        practica7 ventana = new practica7();
    }

















    //<-- Metodo para el telcado -->

    
    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        
        //para rotar
        if (e.getKeyCode() == KeyEvent.VK_A) {
            rotacion[0] = rotacion[0] - Math.PI/16;
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            rotacion[0] = rotacion[0] + Math.PI/16;
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            rotacion[1] = rotacion[1] + Math.PI/16;
        }
        if (e.getKeyCode() == KeyEvent.VK_W) {
            rotacion[1] = rotacion[1] - Math.PI/16;
        }
        if (e.getKeyCode() == KeyEvent.VK_Q) {
            rotacion[2] = rotacion[2] - Math.PI/16;
        }
        if (e.getKeyCode() == KeyEvent.VK_E) {
            rotacion[2] = rotacion[2] + Math.PI/16;
        }
        //para transladarlo
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            traslacion[0] = traslacion[0] - 50;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            traslacion[0] = traslacion[0] + 50;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            traslacion[1] = traslacion[1] + 50;
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            traslacion[1] = traslacion[1] - 50;
        }
        if (e.getKeyCode() == KeyEvent.VK_1) {
            traslacion[2] = traslacion[2] - 50;
        }
        if (e.getKeyCode() == KeyEvent.VK_2) {
            traslacion[2] = traslacion[2] + 50;
        }
        //no sé para que pero escalarlo
        if (e.getKeyCode() == KeyEvent.VK_F) {
            escalacion[0] = escalacion[0] + 0.01;
            escalacion[1] = escalacion[1] + 0.01;
            escalacion[2] = escalacion[2] + 0.01;
        }
        if (e.getKeyCode() == KeyEvent.VK_G) {
            escalacion[0] = escalacion[0] - 0.01;
            escalacion[1] = escalacion[1] - 0.01;
            escalacion[2] = escalacion[2] - 0.01;
        }
        pantalla();
    }

    public void keyReleased(KeyEvent e) {

    }

    //<-- Metodos para dibujar -->
    //escalacion
    public void escalar(int [][] cubo, double Sx, double Sy, double Sz){
        for(int x1=0; x1<=cubo.length - 1; x1++){
            double r[]={0,0,0,0};
            double [] P = {cubo[x1][0], cubo[x1][1],cubo[x1][2], cubo[x1][3]};
            double [][] T = {
                {Sx,0,0,0},
                {0,Sy,0,0},
                {0,0,Sz,0},
                {0,0,0,1}
            };
            int i,j;
            for(i=0;i<4;i++){
                for(j=0;j<4;j++){
                    r[i] += P[j]*T[i][j];
                }
            }
            cubo[x1][0]=(int)r[0];
            cubo[x1][1]=(int)r[1];
            cubo[x1][2]=(int)r[2];
            cubo[x1][3]=(int)r[3];
        }
    }
    //rotar
    public void rotar(int [][] cubo, double Ax, double Ay, double Az){
        //Rotacion X
        for(int x1=0; x1<=cubo.length - 1; x1++){
            double r[]={0,0,0,0};
            double [] P = {cubo[x1][0], cubo[x1][1],cubo[x1][2], cubo[x1][3]};
            double [][] T = {
                {Math.cos(Ax),-Math.sin(Ax),0,0},
                {Math.sin(Ax),Math.cos(Ax),0,0},
                {0,0,1,0},
                {0,0,0,1}
            };
            int i,j;
            for(i=0;i<4;i++){
                for(j=0;j<4;j++){
                    r[i] += P[j]*T[i][j];
                }
            }
            cubo[x1][0]=(int)r[0];
            cubo[x1][1]=(int)r[1];
            cubo[x1][2]=(int)r[2];
            cubo[x1][3]=(int)r[3];
        }
        //Rotacion Y
        for(int x1=0; x1<=cubo.length - 1; x1++){
            double r[]={0,0,0,0};
            double [] P = {cubo[x1][0], cubo[x1][1],cubo[x1][2], cubo[x1][3]};
            double [][] T = {
                {Math.cos(Ay), 0, Math.sin(Ay), 0},
                {0, 1, 0, 0},
                {-Math.sin(Ay), 0, Math.cos(Ay), 0},
                {0, 0, 0, 1}
            };
            int i,j;
            for(i=0;i<4;i++){
                for(j=0;j<4;j++){
                    r[i] += P[j]*T[i][j];
                }
            }
            cubo[x1][0]=(int)r[0];
            cubo[x1][1]=(int)r[1];
            cubo[x1][2]=(int)r[2];
            cubo[x1][3]=(int)r[3];
        }

        //Rotacion en Z
        for(int x1=0; x1<=cubo.length - 1; x1++){
            double r[]={0,0,0,0};
            double [] P = {cubo[x1][0], cubo[x1][1],cubo[x1][2], cubo[x1][3]};
            double [][] T = {
                {1,0,0,0},
                {0,Math.cos(Az),-Math.sin(Az),0},
                {0,Math.sin(Az),Math.cos(Az),0},
                {0,0,0,1}
            };
            int i,j;
            for(i=0;i<4;i++){
                for(j=0;j<4;j++){
                    r[i] += P[j]*T[i][j];
                }
            }
            cubo[x1][0]=(int)r[0];
            cubo[x1][1]=(int)r[1];
            cubo[x1][2]=(int)r[2];
            cubo[x1][3]=(int)r[3];
        }
    }
    //translacion
    public void trasladar(int [][] cubo, int dx,int dy, int dz){
        for(int x1=0; x1<=cubo.length - 1; x1++){
            int r[]={0,0,0,0};
            int [] P = {cubo[x1][0], cubo[x1][1],cubo[x1][2], cubo[x1][3]};
            int [][] T = {{1,0,0,dx},
                          {0,1,0,dy},
                          {0,0,1,dz},
                          {0,0,0,1}};
            int i,j;
            for(i=0;i<4;i++){
                for(j=0;j<4;j++){
                    r[i] += P[j]*T[i][j];
                }
            }
            cubo[x1][0]=r[0];
            cubo[x1][1]=r[1];
            cubo[x1][2]=r[2];
            cubo[x1][3]=r[3];
        }
    }
    //proyeccion 3d
    public int[] paralelaProy(int X, int Y, int Z, int Xc, int Yc, int Zc){      
        int x2, y2;

        x2 = X + ((Xc * Z)/Zc);
        y2 = Y + ((Yc * Z)/Zc);


        int punto[] = {(int)x2, (int)y2};
        return punto;
    }

    //Lineas
    public void linea(int x0, int y0, int x1, int y1, Color c){
        int dx = x1 - x0;
        int dy = y1 - y0;

        int A = 2 * dy;
        int B = 2 * dy - 2 * dx;
        int p = 2 * dy - dx;

        int steps = Math.abs(dx) > Math.abs(dy) ? Math.abs(dx) : Math.abs(dy);
        float xinc = (float) dx / steps;
        float yinc = (float) dy / steps;

        float x = x0;
        float y = y0;

        for(int k = 1; k <= steps; ++k){
            if(p < 0){
                dibujarPixel(Math.round(x) + 1, Math.round(y), c);
                p = p + A;
            } else {
                dibujarPixel(Math.round(x) + 1, Math.round(y) + 1, c);
                p = p + B;
            }
            x = x + xinc;
            y = y + yinc;
        }
    }
    
    //Pixel
    public void dibujarPixel(int x, int y, Color c){
        imagePixel.setRGB(0, 0, c.getRGB());
        buffer.getGraphics().drawImage(imagePixel, x, y, this);
    }

}
