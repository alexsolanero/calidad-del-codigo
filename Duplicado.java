package procesos;

public class Duplicado {
	public static void main(String [] Args){
	int arreglo1[];
	int arreglo2[];
	arreglo1=new int[5];
	arreglo2=new int[5];
	 
	int suma1 = 0;
	int suma2 = 0;
	int promedio1 = 0;
	int promedio2=0;
	 
	for(int a=0;a<4;a++){
		arreglo1[a]=a;
		arreglo2[a]=a;
	}
	 
	for(int i=0; i < 4; i++){
	
	   suma1 += arreglo1[i];
	}
	promedio1 = suma1 / 4;
	 
	for (int i = 0; i < 4; i++)
	{
	   suma2 += arreglo2[i];
	}
	promedio2 = suma2 / 4;

}
}
