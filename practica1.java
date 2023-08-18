/*
José Daniel Vázquez Franco. 
Practica 1: Proyeccion Paralelo
*/
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JFrame;
import java.awt.Color;
import java.lang.Math;
import java.util.ArrayList;

public class practica1 extends JFrame{

    private BufferedImage buffer;
    private Graphics graPixel;

    //paralelo
    public int xp = 100, yp = 150, zp = 300;

    //cordenadas
    static int[][] puntos = {
        //0    1   2
        {150, 150, 51},  //0 
        {150, 250, 51},  //1
        {250, 150, 51},  //2
        {250, 250, 51},  //3
        {150, 150, 150},//4
        {150, 250, 150},//5
        {250, 250, 150},//6
        {250, 150, 150} //7
    };
    
    public practica1(){

        setTitle("Practica1 Proyeccion Paralela");
        setResizable(false);
        setSize(500, 500);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        buffer = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        
        dibujar();

    }

    public void putPixel(int x, int y, Color c){
        buffer.setRGB(0, 0, c.getRGB());
        this.getGraphics().drawImage(buffer, x, y, this);
    }


    public void dibujar(){
        int transformacion2D[][] = new int [8][2];

        //Vertices del primer cuadrado
        transformacion2D[0] = calcular2D(puntos[0][0], puntos[0][1], puntos[0][2]); 
        transformacion2D[1] = calcular2D(puntos[1][0], puntos[1][1], puntos[1][2]); 
        transformacion2D[2] = calcular2D(puntos[2][0], puntos[2][1], puntos[2][2]);  
        transformacion2D[3] = calcular2D(puntos[3][0], puntos[3][1], puntos[3][2]);  

        //Vertices del segundo cuadrado
        transformacion2D[4] = calcular2D(puntos[4][0], puntos[4][1], puntos[4][2]); 
        transformacion2D[5] = calcular2D(puntos[5][0], puntos[5][1], puntos[5][2]); 
        transformacion2D[6] = calcular2D(puntos[6][0], puntos[6][1], puntos[6][2]);  
        transformacion2D[7] = calcular2D(puntos[7][0], puntos[7][1], puntos[7][2]);  

        puntoMedio(transformacion2D[0][0], transformacion2D[0][1], transformacion2D[1][0], transformacion2D[1][1], Color.black);
        puntoMedio(transformacion2D[1][0], transformacion2D[1][1], transformacion2D[2][0], transformacion2D[3][1], Color.black);
        puntoMedio(transformacion2D[2][0], transformacion2D[2][1], transformacion2D[3][0], transformacion2D[3][1], Color.black);
        puntoMedio(transformacion2D[2][0], transformacion2D[2][1], transformacion2D[0][0], transformacion2D[0][1], Color.black);

        puntoMedio(transformacion2D[4][0], transformacion2D[4][1], transformacion2D[5][0], transformacion2D[5][1], Color.black);
        puntoMedio(transformacion2D[5][0], transformacion2D[5][1], transformacion2D[6][0], transformacion2D[6][1], Color.black);
        puntoMedio(transformacion2D[6][0], transformacion2D[6][1], transformacion2D[7][0], transformacion2D[7][1], Color.black);
        puntoMedio(transformacion2D[7][0], transformacion2D[7][1], transformacion2D[4][0], transformacion2D[4][1], Color.black);

        puntoMedio(transformacion2D[0][0], transformacion2D[0][1], transformacion2D[4][0], transformacion2D[4][1], Color.blue);
        puntoMedio(transformacion2D[1][0], transformacion2D[1][1], transformacion2D[5][0], transformacion2D[5][1], Color.blue);
        puntoMedio(transformacion2D[2][0], transformacion2D[2][1], transformacion2D[7][0], transformacion2D[7][1], Color.blue);
        puntoMedio(transformacion2D[3][0], transformacion2D[3][1], transformacion2D[6][0], transformacion2D[6][1], Color.blue);

       
    }


    public static void main(String[] args){

        practica1 b = new practica1();

    }









    /*----------Funciones de dibujo  -----------*/

    //Funcion para calcular x2 y y2
     public int[] calcular2D(int x1, int y1, int z1){
        
        int x2, y2;
        //Formula
        x2 = x1 + ((xp * z1)/zp);
        y2 = y1 + ((yp * z1)/zp);

        int dot[] = {(int)x2, (int)y2};
        return dot;

    }

    //Funcion de lineas
    public void puntoMedio(int x0, int y0, int x1, int y1, Color c){

        float x, y, dx, dy, incx = 1, incy = 1, incE, incNE, p = 0;

        x = x0;
        y = y0;

        dx = x1 - x0;
        dy = y1 - y0;

        if(dx < 0){
            dx = -dx;
            incx = -1;
        }
        if(dy < 0){
            dy = -dy;
            incy = -1;
        }

        if(Math.abs(dx) > Math.abs(dy)){ 
            incE = 2 * dy;
            incNE = 2 * (dy - dx);
            while(x != x1){
                putPixel((int)Math.round(x), (int)Math.round(y), c);
                x = x + incx;
                if(2 * (p + dy) < dx){
                    p = p + incE;
                }else{
                    p = p + incNE;
                    y = y + incy;
                }
            }
        }else{
            incE = 2 * dx;
            incNE = 2 * (dx - dy);
            while(y != y1){
                putPixel((int)Math.round(x), (int)Math.round(y), c);
                y = y + incy;
                if(2 * (p + dx) < dy){
                    p = p + incE;
                }else{
                    p = p + incNE;
                    x = x + incx;
                }
            }
        } 

    }
}
