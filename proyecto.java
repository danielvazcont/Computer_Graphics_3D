/*
José Daniel Vázquez Franco.  6M   #19310205
Proyecto: PACMAN
*/
import java.awt.*;
import java.awt.event.*;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;


import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.Math;
import java.util.Arrays;

public class proyecto extends JFrame implements Runnable, KeyListener {
    private BufferedImage imagePixel;
    private JPanel area;
    private JTextArea textArea;

    //rolas
    int contadorM=0;
    File inicio = new File("inicio.wav");
    File fondo1 = new File("fondo111.wav");
    File waka = new File("waka.wav");
    File fruta = new File("frut.wav");
    File ghost = new File("ghost.wav");
    
    private int rangoMax = 0;
    private int rangoMaxB = 0;
    private int rangoMaxC = 0;

    //Hilo
    int tiempo=1000,tiempoC=1;//tiempo
    boolean unpause=false, reinicio=false, regreso=false, corazonM=false;//pausa
    int contador=0, contador2=0;
    int perspectiva=0;
    private Thread hilo;
    double rotacionA=0;

    private BufferedImage buffer;
    private Image fondo;

    
    //Velocidades y distancia
    int bola=560; //distancia bola
    double velocidadC=0.045;//velocidad de cierre
    int velocidadT=10;//velocidad de translacion bola

    //pacman y fantasma
    int suich=0;

    //Variable para el grosor del pixel
    private int grosor = 1;

    //Puntos del pacman
    private final int cantidad_puntos = 80; //cantidad
    private int profundidad = 80;   //profundidad

    //Puntos de la comida
    private final int cantidad_puntosB = 30; //cantidad
    private int profundidadB = 20;   //profundidad
    int curvaB[][] = new int[cantidad_puntosB][4];

    //Puntos del corazon
    private final int cantidad_puntosC = 60; //cantidad
    private int profundidadC = 2;   //profundidad
    int curvaC[][] = new int[cantidad_puntosC][4];

    //color corazon
    private float color = 0.0F;
    private int recorrer = 0;

    //puntos 3d de la curva
    int curva[][] = new int[cantidad_puntos][4];
    private int curvaminimoX = 0, curvaminimoXB = 0,curvaminimoXC = 0;
    private int curvaminimoY = 0, curvaminimoYB = 0,curvaminimoYC = 0;
    private int curvaminimoZ = 0, curvaminimoZB=0, curvaminimoZC=0;
    private double abrir=1.3;

    //pacman
    int superficie[][][] = new int [profundidad][cantidad_puntos][4];
    double[] escalacion = {0.2,0.2,0.2}; //Escalacion
    double[] rotacion = {0, 7.561282552275765, 1.1780972450961722}; //Rotacion
    int[] traslacion = {420, 270, 0}; //Translacion
    
    //bola
    int superficieB[][][] = new int [profundidad][cantidad_puntos][4];
    double[] escalacionB = {0.04,0.04,0.04}; //Escalacion
    double[] rotacionB = {0, 6.383185307179591, 3}; //Rotacion
    int[] traslacionB = {-bola, 0, 0}; //Translacion

    //corazon
    int superficieC[][][] = new int [profundidadC][cantidad_puntosC][4];
    double[] escalacionC = {0.4,0.4,0.4}; //Escalacion
    double[] rotacionC = {0, 0, 3}; //Rotacion
    int[] traslacionC = {700, 60, 0}; //Translacion
   
    //Camara
    int[] paraleloCam = {0,0,100};
    

    //Constructor
    public proyecto(){
        area = new JPanel();
        textArea = new JTextArea();
        textArea.addKeyListener(this);
        getContentPane().add(textArea, BorderLayout.CENTER);
        getContentPane().add(area, BorderLayout.CENTER);

        setTitle("Practica PACMAN");
        setResizable(true);
		setSize(800,600);
		setVisible(true);
        setBackground(Color.black);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        fondo = area.createImage(area.getWidth(), area.getHeight());
        imagePixel = new BufferedImage(grosor, grosor, BufferedImage.TYPE_INT_RGB);

        pantalla();
        pantalla();

        hilo = new Thread(this);
        
        sonido(inicio);
    }

    //sonido
    static void sonido(File Sound){
        try{
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(Sound));
            
            clip.start();
            
        } catch (Exception e){
            System.out.println("Error Audio");
        }
    }


   
    //Dibujar
    public void pantalla(){
        buffer = new BufferedImage(area.getWidth(), area.getHeight(), BufferedImage.TYPE_INT_RGB);
        buffer.getGraphics().drawImage(fondo, 0, 0, this);
        
        Color col = Color.WHITE;
        curva_parametrica(curva);
        curva_parametricaCOMIDA(curvaB);
        curva_parametricaCORAZON(curvaC);
        //pacman
        for (int i=0; i<profundidad; i++){
            escalacion(superficie[i], escalacion[0], escalacion[1], escalacion[2]);
            rotacion(superficie[i], rotacion[0], rotacion[1], rotacion[2]);
            translacion(superficie[i], traslacion[0], traslacion[1], traslacion[2]);
        }
        //comida
        for (int i=0; i<profundidadB; i++){
            escalacion(superficieB[i], escalacion[0], escalacion[1], escalacion[2]);
            translacion(superficieB[i], traslacionB[0], traslacionB[1], traslacionB[2]);
            rotacion(superficieB[i], 0, 0, 1.8219027549038278);
            rotacion(superficieB[i], rotacion[0], rotacionB[1], rotacion[2]);
            translacion(superficieB[i], traslacion[0], traslacion[1], traslacion[2]);
        }
        //corazon
        for (int i=0; i<profundidadC; i++){
            rotacion(superficieC[i], rotacionC[0], rotacionC[1], rotacionC[2]);
            escalacion(superficieC[i], escalacionC[0], escalacionC[1], escalacionC[2]);
            translacion(superficieC[i], traslacionC[0], traslacionC[1], traslacionC[2]);
        }
        //Dibujar pacman
        if(suich==0){
            col=Color.WHITE;
        }
        if(suich==1){
            col=Color.BLUE;
        }
        if(suich==2){
            col=Color.RED;
        }
        comida(superficieB, paraleloCam, col);
        pacman(superficie, paraleloCam, Color.yellow);
        if(corazonM){
        corazon(superficieC, paraleloCam, Color.getHSBColor(color, 1F, 1F));
        }

        area.getGraphics().drawImage(buffer, 0, 0, this);
    }


    public static void main(String[] args) {
        proyecto ventana = new proyecto();
        
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


    /*<-------------------------------------------------------------------------------------------
    ----------------------------------------------------------------------------------------------
    ----------------------------------------------------------------------------------------------
    ----------------------------------------------------------------------------------------------
    */
    //PACMAN
    //Calcular curva parametrica
    public void curva_parametrica(int curva[][]){
        double tempX, tempY, tempZ;
        double maximoX = 0, minimoX = 0, maximoY = 0, minimoY = 0, maximoZ = 0, minimoZ = 0;
        
        //de
        double t = 0;
        //hasta
        double incr =  1.1 * Math.PI / cantidad_puntos;
        
        for (int i = 0; i < cantidad_puntos; i++) {
           
            //funciones parametricas
            t = incr * i;

            tempY = (10 * Math.cos(t) * 50);
            tempX = -(10 * Math.sin(t)) * 50;
            
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

    
        paraleloCam[2] = rangoMax;

        int temp[][] = {{0,0,0,1}};
        for (int i = 0; i < profundidad; i++) {
            for (int j = 0; j < cantidad_puntos; j++){
                temp[0][0] = curva[j][0];
                temp[0][1] = curva[j][1];
                temp[0][2] = curva[j][2];
                temp[0][3] = curva[j][3];

                //if(i<8){
                rotacion(temp, 0, abrir * Math.PI/profundidad * i, 0);
                //}
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
    public void pacman(int superficie[][][], int cam[], Color c){
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
                linea(superficie2D[i-1][j-1][0], superficie2D[i-1][j-1][1], superficie2D[i][j-1][0], superficie2D[i][j-1][1], c);
            
                if(j == cantidad_puntos - 1) {
                    linea(superficie2D[i-1][j][0], superficie2D[i-1][j][1], superficie2D[i][j][0], superficie2D[i][j][1], c);
                }
                
                if(i == profundidad - 1){
                    //Curva original
                    linea(superficie2D[i][j-1][0], superficie2D[i][j-1][1], superficie2D[i][j][0], superficie2D[i][j][1], c);
                    
                }
                
            }
        
        }
    }
    /*<-------------------------------------------------------------------------------------------
    ----------------------------------------------------------------------------------------------
    ----------------------------------------------------------------------------------------------
    ----------------------------------------------------------------------------------------------
    */
    //corazon
    //Calcular curva parametrica
    public void curva_parametricaCORAZON(int curvaC[][]){
        double tempX, tempY, tempZ;
        double maximoX = 0, minimoX = 0, maximoY = 0, minimoY = 0, maximoZ = 0, minimoZ = 0;
        
        //de
        double t = -Math.PI;
        //hasta
        double incr =  2 * Math.PI / cantidad_puntosC;
        
        for (int i = 0; i < cantidad_puntosC; i++) {
           
            //funciones parametricas
            t = incr * i;

            tempX = ((12 * Math.sin(t)) - (4*Math.sin(3 * t)) )*10;
            tempY = ((13 * Math.cos(t)) - (5*Math.cos(2 * t)) - (2*Math.cos(3 * t))- (Math.cos(4 * t)))*10;

            
            curvaC[i][0] = (int)tempX;
            curvaC[i][1] = (int)tempY;
            curvaC[i][2] = 0;
            curvaC[i][3] = 1;

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

        curvaminimoXC = (int)Math.round(minimoX);
        curvaminimoXC = (int)Math.round(minimoY);
        curvaminimoXC = (int)Math.round(minimoZ);

        if((rangoX) > (rangoY)){
            if((rangoX) > (rangoZ)){
                rangoMaxC = (int)Math.round(rangoX);
            }
            else{
                rangoMaxC = (int)Math.round(rangoZ);
            }
        }
        else if((rangoY) > (rangoZ)){
            rangoMaxC = (int)Math.round(rangoY);
        }
        else{
            rangoMaxC = (int)Math.round(rangoZ);
        }

        
        paraleloCam[2] = rangoMaxC;

        int temp[][] = {{0,0,0,1}};
        for (int i = 0; i < profundidadC; i++) {
            for (int j = 0; j < cantidad_puntosC; j++){
                temp[0][0] = curvaC[j][0];
                temp[0][1] = curvaC[j][1];
                temp[0][2] = curvaC[j][2];
                temp[0][3] = curvaC[j][3];

                //if(i<8){
                rotacion(temp, 0, 2 * Math.PI/profundidadC * i, 0);
                //}
                superficieC[i][j][0] = temp[0][0];
                superficieC[i][j][1] = temp[0][1];
                superficieC[i][j][2] = temp[0][2];
                superficieC[i][j][3] = temp[0][3];

            }
        }
    }

    //Dibujar la curva
    public void dibujarCurvaCORAZON(int curvaC[][], int cam[], Color c){
        int curva2DC[][] = new int [curvaC.length][2];
        
        for (int i = 0; i < curvaC.length; i++) {
            curva2DC[i] = paralelaProy(curvaC[i][0], curvaC[i][1], curvaC[i][2], cam[0], cam[1], cam[2]);
        }
        
        for (int i = 1; i < curvaC.length; i++) {
            linea(curva2DC[i-1][0], curva2DC[i-1][1], curva2DC[i][0], curva2DC[i][1], c);
        }
    }
    //funcion para dibujar la superficie
    public void corazon(int superficieC[][][], int cam[], Color c){
        int superficie2DC[][][] = new int [profundidadC][cantidad_puntosC][3];
        
        for (int i = 0; i < profundidadC; i++) {
            for (int j = 0; j < cantidad_puntosC; j++){
                superficie2DC[i][j] = paralelaProy(superficieC[i][j][0], superficieC[i][j][1], superficieC[i][j][2], cam[0], cam[1], cam[2]);
            }
        }

        for (int i = 1; i < profundidadC; i++) {
        
            for (int j = 1; j < cantidad_puntosC; j++){
                
                //Curva original
                linea(superficie2DC[i-1][j-1][0], superficie2DC[i-1][j-1][1], superficie2DC[i-1][j][0], superficie2DC[i-1][j][1], c);
            
                if(j == cantidad_puntosC - 1) {
                    linea(superficie2DC[i-1][j][0], superficie2DC[i-1][j][1], superficie2DC[i][j][0], superficie2DC[i][j][1], c);
                }
                
                
            }
        
        }
    }
    /*<-------------------------------------------------------------------------------------------
    ----------------------------------------------------------------------------------------------
    ----------------------------------------------------------------------------------------------
    ----------------------------------------------------------------------------------------------
    */
    //COMIDA
    //Calcular curva parametrica
    public void curva_parametricaCOMIDA(int curvaB[][]){
        double tempX, tempY, tempZ;
        double maximoX = 0, minimoX = 0, maximoY = 0, minimoY = 0, maximoZ = 0, minimoZ = 0;
        
        //de
        double t = 0;
        //hasta
        double incr =  2 * Math.PI / cantidad_puntosB;
        
        for (int i = 0; i < cantidad_puntosB; i++) {
           
            //funciones parametricas
            t = incr * i;

            tempY = (10 * Math.cos(t) * 15);
            tempX = -(10 * Math.sin(t)) * 15;

            if(suich==0){
            tempY = (10 * Math.cos(t) * 15);
            tempX = -(10 * Math.sin(t)) * 15;
            }
            else if(suich==1){
            tempX = (200 * Math.cos(t/2));
            tempY = -(200 * Math.cos(t));
            }
            else if(suich==2){
            tempY = (200 * Math.cos(t/2));
            tempX = -(200 * Math.cos(t));
            }
            
            curvaB[i][0] = (int)tempX;
            curvaB[i][1] = (int)tempY;
            curvaB[i][2] = 0;
            curvaB[i][3] = 1;

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

        curvaminimoXB = (int)Math.round(minimoX);
        curvaminimoXB = (int)Math.round(minimoY);
        curvaminimoXB = (int)Math.round(minimoZ);

        if((rangoX) > (rangoY)){
            if((rangoX) > (rangoZ)){
                rangoMaxB = (int)Math.round(rangoX);
            }
            else{
                rangoMaxB = (int)Math.round(rangoZ);
            }
        }
        else if((rangoY) > (rangoZ)){
            rangoMaxB = (int)Math.round(rangoY);
        }
        else{
            rangoMaxB = (int)Math.round(rangoZ);
        }

        
        paraleloCam[2] = rangoMaxB;

        int temp[][] = {{0,0,0,1}};
        for (int i = 0; i < profundidadB; i++) {
            for (int j = 0; j < cantidad_puntosB; j++){
                temp[0][0] = curvaB[j][0];
                temp[0][1] = curvaB[j][1];
                temp[0][2] = curvaB[j][2];
                temp[0][3] = curvaB[j][3];

                //if(i<8){
                rotacion(temp, 0, 2 * Math.PI/profundidadB * i, 0);
                //}
                superficieB[i][j][0] = temp[0][0];
                superficieB[i][j][1] = temp[0][1];
                superficieB[i][j][2] = temp[0][2];
                superficieB[i][j][3] = temp[0][3];

            }
        }
    }

    //Dibujar la curva
    public void dibujarCurvaCOMIDA(int curvaB[][], int cam[], Color c){
        int curva2DB[][] = new int [curvaB.length][2];

        for (int i = 0; i < curvaB.length; i++) {
            curva2DB[i] = paralelaProy(curvaB[i][0], curvaB[i][1], curvaB[i][2], cam[0], cam[1], cam[2]);
        }
        
        for (int i = 1; i < curvaB.length; i++) {
            linea(curva2DB[i-1][0], curva2DB[i-1][1], curva2DB[i][0], curva2DB[i][1], c);
        }
    }
    //funcion para dibujar la superficie
    public void comida(int superficieB[][][], int cam[], Color c){
        int superficie2DB[][][] = new int [profundidadB][cantidad_puntosB][3];
        
        for (int i = 0; i < profundidadB; i++) {
            for (int j = 0; j < cantidad_puntosB; j++){
                superficie2DB[i][j] = paralelaProy(superficieB[i][j][0], superficieB[i][j][1], superficieB[i][j][2], cam[0], cam[1], cam[2]);
            }
        }

        for (int i = 1; i < profundidadB; i++) {
        
            for (int j = 1; j < cantidad_puntosB; j++){
                
                //Curva original
                linea(superficie2DB[i-1][j-1][0], superficie2DB[i-1][j-1][1], superficie2DB[i-1][j][0], superficie2DB[i-1][j][1], c);
                linea(superficie2DB[i-1][j-1][0], superficie2DB[i-1][j-1][1], superficie2DB[i][j-1][0], superficie2DB[i][j-1][1], c);
            
                if(j == cantidad_puntosB - 1) {
                    linea(superficie2DB[i-1][j][0], superficie2DB[i-1][j][1], superficie2DB[i][j][0], superficie2DB[i][j][1], c);
                }
                
                if(i == profundidadB - 1){
                    //Curva original
                    linea(superficie2DB[i][j-1][0], superficie2DB[i][j-1][1], superficie2DB[i][j][0], superficie2DB[i][j][1], c);
                    //Linea 1 y 2 diagonal
                    linea(superficie2DB[0][j-1][0], superficie2DB[0][j-1][1], superficie2DB[i][j-1][0], superficie2DB[i][j-1][1], c);
                    //linea(superficie2D[0][j][0], superficie2D[0][j][1], superficie2D[i][j-1][0], superficie2D[i][j-1][1], c);
                    if(j == cantidad_puntosB - 1) {
                        linea(superficie2DB[0][j][0], superficie2DB[0][j][1], superficie2DB[i][j][0], superficie2DB[i][j][1], c);
                    }
                }
                
            }
        
        }
    }

    
     //<-- Metodos para el teclado -->

    
     public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        
         //para rotar

        if (e.getKeyCode() == KeyEvent.VK_S) {
            rotacion[1] = rotacion[1] + Math.PI/16;
            rotacionB[1] = rotacionB[1] + Math.PI/16;
        }
        if (e.getKeyCode() == KeyEvent.VK_W) {
            rotacion[1] = rotacion[1] - Math.PI/16;
            rotacionB[1] = rotacionB[1] - Math.PI/16;
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            rotacion[2] = rotacion[2] - Math.PI/16;
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            rotacion[2] = rotacion[2] + Math.PI/16;
        }
        
        //Reiniciar
        if (e.getKeyCode() == KeyEvent.VK_R) {
            rotacion[0] = 0; rotacion[1] = 7.561282552275765; rotacion[2] = 1.1780972450961722;
            rotacionB[0] = 0; rotacionB[1] = 6.383185307179591; rotacionB[2] = 0;
            traslacionB[0]=-bola;
            reinicio=false;
            contador=0;
            pantalla();
        }
        
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
        // detener hilo 
        if (e.getKeyCode() == KeyEvent.VK_M) {
            unpause=false;
        }
        // continuar hilo 
        if (e.getKeyCode() == KeyEvent.VK_N) {
            unpause=true;
            hilo.start();
        }
        // mostrar corazon
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            corazonM=true;
        }

        // ocultar corazon
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            corazonM=false;
        }
        // cambiar a bola 
        if (e.getKeyCode() == KeyEvent.VK_E) {
            if(suich==0){
                suich=1;
            }
            else if(suich==1){
                suich=2;
            }
            else if(suich==2){
                suich=0;
            }
        }
        // cambiar a fantasma 
        if (e.getKeyCode() == KeyEvent.VK_Q) {
            if(suich==0){
                suich=2;
            }
            else if(suich==1){
                suich=0;
            }
            else if(suich==2){
                suich=1;
            }
        }
        
    }

    public void keyReleased(KeyEvent e) {

    }




    @Override
    public void run() {
        sonido(fondo1);
        while(true){
            try{
                //inicio del hilo
                if (color > 0.98){
                    recorrer = 1;
                }
                else if(color < 0.01){
                    recorrer = 0;
                }
                
                if (recorrer == 0){
                    color = color + 0.01F;
                }
                else{
                    color = color - 0.01F;
                }
                if(unpause){
                    tiempo=tiempoC;

                    traslacionB[0] = traslacionB[0] + velocidadT;

                    if(traslacionB[0]==-150){
                        rotacionA = rotacion[1];
                        reinicio=true;
                    }
                    if(traslacionB[0]==-bola + velocidadT){
                        regreso=true;
                    }
                    //abrir y cerrar boca
                    if(reinicio){
                        contador2++;
                        regreso=false;
                        abrir=abrir + velocidadC;
                        rotacion[1] = rotacion[1] - velocidadC;
                    }
                    if(regreso && contador!=0){
                        contador2--;
                        abrir=abrir - velocidadC;
                        rotacion[1] = rotacion[1] + velocidadC;
                        if(contador2==0){
                            contador=0;
                        }
                    }
                    

                    if(traslacionB[0]>=0){
                        traslacionB[0] = traslacionB[0] - bola;
                        reinicio=false;
                        contador++;
                        if(suich==0){
                            sonido(waka);
                        }
                        else if(suich==1){
                            sonido(ghost);
                        }
                        else if(suich==2){
                            sonido(fruta);
                        }
                       
                    }
                    
                
                }else{
                    tiempo=tiempoC;
                }
                pantalla();
                hilo.sleep(tiempo);
            }catch(InterruptedException ex){

            }
        }
        
    }
}
