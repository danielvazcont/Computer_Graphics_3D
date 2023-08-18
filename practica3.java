/*
José Daniel Vázquez Franco.  
Practica 3: Translación 3D
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.lang.Math;

public class practica3 extends JFrame implements KeyListener {
    private BufferedImage imagePixel;
    private JPanel area;
    private JTextArea textArea;

    private BufferedImage buffer;
    private Image fondo;
    private int grosor = 1;

    //translacion
    int[] traslacion = {140, 100, 0};
    
    //Proyeccion
    int[] vista = {150, -100, 500};

    //coordenadas
    int[][] puntos = {
        //0    1    2   4
        {150, 150, 51, 1},  //0 
        {150, 250, 51, 1},  //1
        {250, 250, 51, 1},  //2
        {250, 150, 51, 1},  //3
        {150, 150, 250, 1}, //4
        {150, 250, 250, 1}, //5
        {250, 250, 250, 1}, //6
        {250, 150, 250, 1}  //7
    };
    
    //translado puntos iniciales
    int puntosTranslacion[][] = new int[8][4];

    //Constructor
    public practica3(){
        area = new JPanel();
        textArea = new JTextArea();
        textArea.addKeyListener(this);
        getContentPane().add(textArea, BorderLayout.CENTER);
        getContentPane().add(area, BorderLayout.CENTER);


        setTitle("Practica3 Traslación");
        setResizable(false);
		setSize(800,600);
        setLocationRelativeTo(null);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        fondo = area.createImage(area.getWidth(), area.getHeight());
        imagePixel = new BufferedImage(grosor, grosor, BufferedImage.TYPE_INT_RGB);

        dibujar();
        dibujar();
    }

    //Pixel
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
                puntosTranslacion[i][j] = puntos[i][j];
            }
        }

        //cubo
        translacionn(puntosTranslacion, traslacion[0], traslacion[1], traslacion[2]);
        dibujarCubo(puntosTranslacion, vista, Color.BLUE);

        area.getGraphics().drawImage(buffer, 0, 0, this);
    }

    
    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_A) {
            traslacion[0] = traslacion[0] - 5;
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            traslacion[0] = traslacion[0] + 5;
        }
        if (e.getKeyCode() == KeyEvent.VK_W) {
            traslacion[1] = traslacion[1] - 5;
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            traslacion[1] = traslacion[1] + 5;
        }
        if (e.getKeyCode() == KeyEvent.VK_Q) {
            traslacion[2] = traslacion[2] - 5;
        }
        if (e.getKeyCode() == KeyEvent.VK_E) {
            traslacion[2] = traslacion[2] + 5;
        }
        dibujar();
    }

    public void keyReleased(KeyEvent e) {

    }

    public static void main(String[] args) {
        practica3 ventana = new practica3();
    }







/*----------Funciones de dibujo  -----------*/

//Transladar Cubo
    public void translacionn(int [][] cubo, int dx,int dy, int dz){
        for(int x1=0; x1<=7; x1++){
            int r[]={0,0,0,0};
            int [] P = {cubo[x1][0], cubo[x1][1],cubo[x1][2], cubo[x1][3]};
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
            cubo[x1][0]=r[0];
            cubo[x1][1]=r[1];
            cubo[x1][2]=r[2];
            cubo[x1][3]=r[3];
        }
    }
    //pasar 3d a 2d
    public int[] calcular2D(int X, int Y, int Z, int Xc, int Yc, int Zc){      
        int x2, y2;

        x2 = X + ((Xc * Z)/Zc);
        y2 = Y + ((Yc * Z)/Zc);

        int punto[] = {(int)x2, (int)y2};
        return punto;
    }
    
    //Cubo
    public void dibujarCubo(int puntos[][], int cam[], Color c){
        int transformacion2D[][] = new int [8][2];

        for (int i = 0; i < 8; i++) {
            transformacion2D[i] = calcular2D(puntos[i][0], puntos[i][1], puntos[i][2], cam[0], cam[1], cam[2]);
        }

        dibujarLinea(transformacion2D[0][0], transformacion2D[0][1], transformacion2D[1][0], transformacion2D[1][1], Color.black);
        dibujarLinea(transformacion2D[1][0], transformacion2D[1][1], transformacion2D[2][0], transformacion2D[2][1], Color.black);
        dibujarLinea(transformacion2D[2][0], transformacion2D[2][1], transformacion2D[3][0], transformacion2D[3][1], Color.black);
        dibujarLinea(transformacion2D[3][0], transformacion2D[3][1], transformacion2D[0][0], transformacion2D[0][1], Color.black);

        dibujarLinea(transformacion2D[0][0], transformacion2D[0][1], transformacion2D[4][0], transformacion2D[4][1], Color.blue);
        dibujarLinea(transformacion2D[1][0], transformacion2D[1][1], transformacion2D[5][0], transformacion2D[5][1], Color.blue);
        dibujarLinea(transformacion2D[2][0], transformacion2D[2][1], transformacion2D[6][0], transformacion2D[6][1], Color.blue);
        dibujarLinea(transformacion2D[3][0], transformacion2D[3][1], transformacion2D[7][0], transformacion2D[7][1], Color.blue);

        dibujarLinea(transformacion2D[4][0], transformacion2D[4][1], transformacion2D[5][0], transformacion2D[5][1], Color.black);
        dibujarLinea(transformacion2D[5][0], transformacion2D[5][1], transformacion2D[6][0], transformacion2D[6][1], Color.black);
        dibujarLinea(transformacion2D[6][0], transformacion2D[6][1], transformacion2D[7][0], transformacion2D[7][1], Color.black);
        dibujarLinea(transformacion2D[7][0], transformacion2D[7][1], transformacion2D[4][0], transformacion2D[4][1], Color.black);
        
    }
    //linea
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
}


