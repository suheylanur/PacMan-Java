import javax.swing.JFrame;
//java da pencere oluşturmak için kullanılan bir swing sınıfı

public class Main {
    public static void main(String[] args) throws Exception {
        int rowCount=21; //21 satır
        int columnCount=32;//32 sütun
        int tileSize=32;//her kare 32x32 piksel
        int boardWidth=columnCount*tileSize;
        int boardHeight=rowCount*tileSize;
        JFrame frame=new JFrame("PAC MAN");
        frame.setVisible(true);
        frame.setSize(boardWidth,boardHeight); //pencere boyutu
        frame.setLocationRelativeTo(null);//pencere tam ortada çıkar
        frame.setResizable(false);//kullanıcı pencereyi istediği kadar küçültüp büyütür
        //grafik bozulur
        //oranlar sabit kalmaz false yapmak en güzeli
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //kullanıcı pencereyi kapatıığında program tamamen sonlanır
        //eğer kapatılmasaydı arka planda çalışmaya devam edebilirdi.

        PacMan pacmanGame=new PacMan();
        frame.add(pacmanGame);
        frame.pack();
        pacmanGame.requestFocus();
        frame.setVisible(true);
    }




    }
