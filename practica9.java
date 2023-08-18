/*
José Daniel Vázquez Franco. 
Practica 9: Cilindro
*/
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.lang.Math;

public class practica9 extends JFrame implements KeyListener {
    private BufferedImage imagePixel;
    private JPanel area;
    private JTextArea textArea;

    private int rangoMax = 0;

    private BufferedImage buffer;
    private Image fondo;

    //Variable para el grosor del pixel
    private int grosor = 1;

    //Puntos de la curva
    private final int cantidad_puntos = 60; //cantidad
    private int profundidad = 20;   //profundidad


    int superficie[][][] = new int [profundidad][cantidad_puntos][4];

    //puntos 3d de la curva
    int curva[][] = new int[cantidad_puntos][4];
    private int curvaminimoX = 0;
    private int curvaminimoY = 0;
    private int curvaminimoZ = 0;

    //Escalacion
    double[] escalacion = {0.3,0.3,0.3};

    //Rotacion
    double[] rotacion = {0, 0, 100};

    //Translacion
    int[] traslacion = {420, 280, 0};

    //inicio Translacion
    int[] traslacionCurva = {200, 200, 0};
    
    //Camara
    int[] paraleloCam = {0,0,100};
    

    //Constructor
    public practica9(){
        area = new JPanel();
        textArea = new JTextArea();
        textArea.addKeyListener(this);
        getContentPane().add(textArea, BorderLayout.CENTER);
        getContentPane().add(area, BorderLayout.CENTER);

        setTitle("Practica 9_Cilindro");
        setResizable(true);
		setSize(800,600);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        fondo = area.createImage(area.getWidth(), area.getHeight());
        imagePixel = new BufferedImage(grosor, grosor, BufferedImage.TYPE_INT_RGB);

        pantalla();
        pantalla();
    }

    


    //Calcular curva parametrica
    public void curva_parametrica(int curva[][]){
        double tempX, tempY, tempZ;
        double maximoX = 0, minimoX = 0, maximoY = 0, minimoY = 0, maximoZ = 0, minimoZ = 0;
        
        //de
        double t =  -Math.PI;
        //hasta
        double incr =  2 * Math.PI / cantidad_puntos;
        
        for (int i = 0; i < cantidad_puntos; i++) {
           
            //funciones parametricas
            t = incr * i;
            //tempX = ((12 * Math.sin(t)) - (4*Math.sin(3 * t)) )*10;
            //tempY = ((13 * Math.cos(t)) - (5*Math.cos(2 * t)) - (2*Math.cos(3 * t))- (Math.cos(4 * t)))*10;
            tempX = (Math.cos(t) * 100) + 200;
            tempY = (-t * 100);
            
            curva[i][0] = (int)tempX;
            curva[i][1] = (int)tempY;
            curva[i][2] = 0;
            curva[i][3] = 1;

            if(i == 0){
                maximoY = tempY;
                minimoY = tempY;
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

        int temp[][] = {{0,0,0,1}};
        for (int i = 0; i < profundidad; i++) {
            for (int j = 0; j < cantidad_puntos; j++){
                temp[0][0] = curva[j][0];
                temp[0][1] = curva[j][1] + rangoMax/2;
                temp[0][2] = curva[j][2];
                temp[0][3] = curva[j][3];

                rotacion(temp, 0, 2 * Math.PI/profundidad * i, 0);

                superficie[i][j][0] = temp[0][0];
                superficie[i][j][1] = temp[0][1];
                superficie[i][j][2] = temp[0][2];
                superficie[i][j][3] = temp[0][3];

            }
        }
    }

    //Dibujar la curva
    public void dibujarCurva(int curva[][], int cam[], Color c){
        int curva2D[][] = new int [curva.length][2];

        for (int i = 0; i < curva.length; i++) {
            curva2D[i] = paralelaProy(curva[i][0], curva[i][1], curva[i][2], cam[0], cam[1], cam[2]);
        }
        
        for (int i = 1; i < curva.length; i++) {
            linea(curva2D[i-1][0], curva2D[i-1][1], curva2D[i][0], curva2D[i][1], c);
        }
    }
    //funcion para dibujar la superficie
    public void superficie_funcion(int superficie[][][], int cam[], Color c){
        int superficie2D[][][] = new int [profundidad][cantidad_puntos][3];
        
        for (int i = 0; i < profundidad; i++) {
            for (int j = 0; j < cantidad_puntos; j++){
                superficie2D[i][j] = paralelaProy(superficie[i][j][0], superficie[i][j][1], superficie[i][j][2], cam[0], cam[1], cam[2]);
            }
        }

        for (int i = 1; i < profundidad; i++) {
            for (int j = 1; j < cantidad_puntos; j++){
                //Curva original
                linea(superficie2D[i-1][j-1][0], superficie2D[i-1][j-1][1], superficie2D[i-1][j][0], superficie2D[i-1][j][1], c);
                //Linea 1 y 2 diagonal
                linea(superficie2D[i-1][j-1][0], superficie2D[i-1][j-1][1], superficie2D[i][j-1][0], superficie2D[i][j-1][1], c);
                //linea(superficie2D[i-1][j-1][0], superficie2D[i-1][j-1][1], superficie2D[i][j][0], superficie2D[i][j][1], c);
                if(j == cantidad_puntos - 1) {
                    linea(superficie2D[i-1][j][0], superficie2D[i-1][j][1], superficie2D[i][j][0], superficie2D[i][j][1], c);
                }
                
                if(i == profundidad - 1){
                    //Curva original
                    //linea(superficie2D[i][j-1][0], superficie2D[i][j-1][1], superficie2D[i][j][0], superficie2D[i][j][1], c);
                    //Linea 1 y 2 diagonal
                    linea(superficie2D[0][j-1][0], superficie2D[0][j-1][1], superficie2D[i][j-1][0], superficie2D[i][j-1][1], c);
                    //linea(superficie2D[0][j][0], superficie2D[0][j][1], superficie2D[i][j-1][0], superficie2D[i][j-1][1], c);
                    if(j == cantidad_puntos - 1) {
                        linea(superficie2D[0][j][0], superficie2D[0][j][1], superficie2D[i][j][0], superficie2D[i][j][1], c);
                    }
                }
            }
        }
    }

   
    //Dibujar
    public void pantalla(){
        buffer = new BufferedImage(area.getWidth(), area.getHeight(), BufferedImage.TYPE_INT_RGB);
        buffer.getGraphics().drawImage(fondo, 0, 0, this);

        curva_parametrica(curva);
        
        for (int i=0; i<profundidad; i++){
            escalacion(superficie[i], escalacion[0], escalacion[1], escalacion[2]);
            rotacion(superficie[i], rotacion[0], rotacion[1], rotacion[2]);
            translacion(superficie[i], traslacion[0], traslacion[1], traslacion[2]);
        }
        
        //Dibujar cilindro
        superficie_funcion(superficie, paraleloCam, Color.BLUE);

        area.getGraphics().drawImage(buffer, 0, 0, this);
    }


    public static void main(String[] args) {
        practica9 ventana = new practica9();
    }








    //<-- Metodos para el teclado -->

    
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
            escalacion[0] = escalacion[0] + 0.1;
            escalacion[1] = escalacion[1] + 0.1;
            escalacion[2] = escalacion[2] + 0.1;
        }
        if (e.getKeyCode() == KeyEvent.VK_G) {
            escalacion[0] = escalacion[0] - 0.1;
            escalacion[1] = escalacion[1] - 0.1;
            escalacion[2] = escalacion[2] - 0.1;
        }
        pantalla();
    }

    public void keyReleased(KeyEvent e) {

    }

    //<-- Metodos para dibujar -->
    //paralea
    public int[] paralelaProy(int X, int Y, int Z, int Xc, int Yc, int Zc){      
        int x2, y2;

        x2 = X + ((Xc * Z)/Zc);
        y2 = Y + ((Yc * Z)/Zc);

        int punto[] = {(int)x2, (int)y2};
        return punto;
    }
    //escalacion
    public void escalacion(int [][] cubo, double Sx, double Sy, double Sz){
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
    //Rotacion
    public void rotacion(int [][] cubo, double Ax, double Ay, double Az){
        //Rotacion x
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
        //Rotacion en Y
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

        //Rotacion en z
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
    //funcion translacion
    public void translacion(int [][] cubo, int dx,int dy, int dz){
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
    //liena
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
