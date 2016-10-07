//Erik Matthews
//Starter Gui workup
//September 29, 2016

package gui; //Change appropriately

import java.awt.GridLayout; import java.awt.event.ActionEvent; import java.awt.event.ActionListener;

import javax.swing.JButton; import javax.swing.JFrame;

public class gui {

public static void main(String[] args) {
	//Creates the frame
    JFrame f = new JFrame();
    //Sets size of frame
    f.setSize(450, 350);
    //Where buttons will be place (rows, columns)
    f.setLayout(new GridLayout(2,5));

    for(int i = 0; i < 10; i++){
        JButton thebutton = new JButton();
        thebutton.setText("Button: " + i);
        thebutton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                System.out.println("You clicked a button");
            }
        });
        thebutton.setSize(10,10);
        f.add(thebutton);
    }
    f.setVisible(true);
}
}