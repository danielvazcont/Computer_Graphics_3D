/*
José Daniel Vázquez Franco.  
Practica 4: Escalamiento 3D
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.lang.Math;

public class practica4 extends JFrame implements KeyListener {
    private BufferedImage imagePixel;
    private JPanel area;
    private JTextArea textArea;

    private BufferedImage buffer;
    private Image fondo;
    private int grosor = 1;

    
    //Escalacion
    double[] escalacion = {1, 1, 1};

    //tranlacion
    int[] traslacion = {400, 250, 0};
    
    //vista paralela
    int[] vista = {150,200,300};

    //puntos del puntos
    int[][] puntos = {
        //0    1    2   3
        {-50, -50, -50, 1}, //0 
        {-50, 50, -50, 1},  //1
        {50, 50, -50, 1},   //2
        {50, -50, -50, 1},  //3
        {-50, -50, 50, 1},  //4
        {-50, 50, 50, 1},   //5
        {50, 50, 50, 1},    //6
        {50, -50, 50, 1}    //7
    };
    
    //translacion
    int puntosEscalacion[][] = new int[8][4];

    //constructor
    public practica4(){
        area = new JPanel();
        textArea = new JTextArea();
        textArea.addKeyListener(this);
        getContentPane().add(textArea, BorderLayout.CENTER);
        getContentPane().add(area, BorderLayout.CENTER);


        setTitle("Practica4 Escalación");
        setResizable(true);
		setSize(800,600);
		setVisible(true);
        setLocationRelativeTo(null);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        fondo = area.createImage(area.getWidth(), area.getHeight());
        imagePixel = new BufferedImage(grosor, grosor, BufferedImage.TYPE_INT_RGB);

        dibujar();
        dibujar();

    }

    //pixel
    public void dibujarPixel(int x, int y, Color c){
        imagePixel.setRGB(0, 0, c.getRGB());
        buffer.getGraphics().drawImage(imagePixel, x, y, this);
    }

    //pintar
    public void dibujar(){
        buffer = new BufferedImage(area.getWidth(), area.getHeight(), BufferedImage.TYPE_INT_RGB);
        buffer.getGraphics().drawImage(fondo, 0, 0, this);

        for(int i=0; i<8; i++){
            for(int j=0; j<4; j++){
                puntosEscalacion[i][j] = puntos[i][j];
            }
        }

        //cubo
        escalacion(puntosEscalacion, escalacion[0], escalacion[1], escalacion[2]);
        translacionn(puntosEscalacion, traslacion[0], traslacion[1], traslacion[2]);
        dibujarCubo(puntosEscalacion, vista, Color.BLUE);

        area.getGraphics().drawImage(buffer, 0, 0, this);
    }
    

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_A) {
            escalacion[0] = escalacion[0] - 0.1;
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            escalacion[0] = escalacion[0] + 0.1;
        }
        if (e.getKeyCode() == KeyEvent.VK_W) {
            escalacion[1] = escalacion[1] - 0.1;
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            escalacion[1] = escalacion[1] + 0.1;
        }
        if (e.getKeyCode() == KeyEvent.VK_Q) {
            escalacion[2] = escalacion[2] - 0.1;
        }
        if (e.getKeyCode() == KeyEvent.VK_E) {
            escalacion[2] = escalacion[2] + 0.1;
        }
        dibujar();
    }

    public void keyReleased(KeyEvent e) {

    }

    public static void main(String[] args) {
        practica4 ventana = new practica4();
    }
    










    /*----------Funciones de dibujo  -----------*/
    //Escalacion
    public void escalacion(int [][] puntos, double Sx, double Sy, double Sz){
        for(int x1=0; x1<=7; x1++){
            double r[]={0,0,0,0};
            double [] P = {puntos[x1][0], puntos[x1][1],puntos[x1][2], puntos[x1][3]};
            double [][] T = {{      Sx,0,0,0    },
                            {       0,Sy,0,0    },
                            {       0,0,Sz,0    },
                            {       0,0,0,1     }};
            int i,j;
            for(i=0;i<4;i++){
                for(j=0;j<4;j++){
                    r[i] += P[j]*T[i][j];
                }
            }
            puntos[x1][0]=(int)r[0];
            puntos[x1][1]=(int)r[1];
            puntos[x1][2]=(int)r[2];
            puntos[x1][3]=(int)r[3];
        }
    }
    //transladar
    public void translacionn(int [][] puntos, int dx,int dy, int dz){
        for(int x1=0; x1<=7; x1++){
            int r[]={0,0,0,0};
            int [] P = {puntos[x1][0], puntos[x1][1],puntos[x1][2], puntos[x1][3]};
            int [][] T = {{     1,0,0,dx    },
                          {     0,1,0,dy    },
                          {     0,0,1,dz    },
                          {     0,0,0,1     }};
            int i,j;
            for(i=0;i<4;i++){
                for(j=0;j<4;j++){
                    r[i] += P[j]*T[i][j];
                }
            }
            puntos[x1][0]=r[0];
            puntos[x1][1]=r[1];
            puntos[x1][2]=r[2];
            puntos[x1][3]=r[3];
        }
    }
    //perspectiva paralela
    public int[] paralelaP(int X, int Y, int Z, int Xc, int Yc, int Zc){      
        int x2, y2;

        x2 = X + ((Xc * Z)/Zc);
        y2 = Y + ((Yc * Z)/Zc);

        int punto[] = {(int)x2, (int)y2};
        return punto;
    }
    //Dibujar linea
    public void dibujarLinea(int x0, int y0, int x1, int y1, Color c){
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

    //Cubo
    public void dibujarCubo(int puntos[][], int cam[], Color c){
        int transformar2D[][] = new int [8][2];

        for (int i = 0; i < 8; i++) {
            transformar2D[i] = paralelaP(puntos[i][0], puntos[i][1], puntos[i][2], cam[0], cam[1], cam[2]);
        }

        dibujarLinea(transformar2D[0][0], transformar2D[0][1], transformar2D[1][0], transformar2D[1][1], Color.black);
        dibujarLinea(transformar2D[1][0], transformar2D[1][1], transformar2D[2][0], transformar2D[2][1], Color.black);
        dibujarLinea(transformar2D[2][0], transformar2D[2][1], transformar2D[3][0], transformar2D[3][1], Color.black);
        dibujarLinea(transformar2D[3][0], transformar2D[3][1], transformar2D[0][0], transformar2D[0][1], Color.black);

        dibujarLinea(transformar2D[0][0], transformar2D[0][1], transformar2D[4][0], transformar2D[4][1], Color.blue);
        dibujarLinea(transformar2D[1][0], transformar2D[1][1], transformar2D[5][0], transformar2D[5][1], Color.blue);
        dibujarLinea(transformar2D[2][0], transformar2D[2][1], transformar2D[6][0], transformar2D[6][1], Color.blue);
        dibujarLinea(transformar2D[3][0], transformar2D[3][1], transformar2D[7][0], transformar2D[7][1], Color.blue);

        dibujarLinea(transformar2D[4][0], transformar2D[4][1], transformar2D[5][0], transformar2D[5][1], Color.black);
        dibujarLinea(transformar2D[5][0], transformar2D[5][1], transformar2D[6][0], transformar2D[6][1], Color.black);
        dibujarLinea(transformar2D[6][0], transformar2D[6][1], transformar2D[7][0], transformar2D[7][1], Color.black);
        dibujarLinea(transformar2D[7][0], transformar2D[7][1], transformar2D[4][0], transformar2D[4][1], Color.black);
        
    }
}
