package org.game.ttt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TicTacToe
{
    protected static Button makebutton(String name, TicTacToePlayer player) {
        Button b = new Button("");
        b.setName(name);
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                b.setLabel("X");
                player.actionPerformed(e);
            }
        });
        return b;
    }


    public static void main(String args[])
    {
        TicTacToePlayer player = new TicTacToePlayer();

        JFrame frame = new JFrame("Tic Tac Toe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setVisible(true);
        frame.getContentPane().setLayout(new GridLayout(3, 3));
        frame.getContentPane().setFont(new Font("SansSerif", Font.PLAIN, 18));
        frame.getContentPane().add(makebutton("A1",player));
        frame.getContentPane().add(makebutton("A2",player));
        frame.getContentPane().add(makebutton("A3",player));
        frame.getContentPane().add(makebutton("B1",player));
        frame.getContentPane().add(makebutton("B2",player));
        frame.getContentPane().add(makebutton("B3",player));
        frame.getContentPane().add(makebutton("C1",player));
        frame.getContentPane().add(makebutton("C2",player));
        frame.getContentPane().add(makebutton("C3",player));
    }
}
