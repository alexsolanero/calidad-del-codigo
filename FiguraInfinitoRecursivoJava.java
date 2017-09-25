import java.util.Scanner;

public class FiguraInfinitoRecursivoJava{
	static Scanner entrada = new Scanner(System.in);
	static void FiguraInfinitoRecursivo(int [][] mat, int nf, int nc){
		
		int mitad=6/2;
		if(nf>=0){
			if(nc==0 || nc==6 || nf==nc || 6-nf == nc)
				System.out.print( mat[nf][nc]);
			else
				System.out.print("-");
			
			nc--;
			if(nc >= 0)
				FiguraInfinitoRecursivo(mat, nf, nc);
			else{
				System.out.println();
				FiguraInfinitoRecursivo(mat, nf-1,6);
			}
		}
	}
	public static void main(String[] args){
		int [][] mat=new int [7][7];
		
		for(int i=0; i<7; i++){
			for(int j=0; j<7; j++)
				mat[i][j]=(int) (Math.random()*9);
		}
		figuraInfinitoRecursivo(mat,6,6);
	}
}
	
