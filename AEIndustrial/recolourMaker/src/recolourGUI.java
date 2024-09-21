import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class recolourGUI extends Application
{
    // gamedata should always be two folders above tha program
    private final File gameData = new File("../..");

    private JFrame window;
    private GridBagLayout layout = new GridBagLayout();
    private int width, height;

    private JButton workingDirectorySelectButton;
    private JButton recolourFileSelectButton;
    private JButton defaultFileSelectButton;
    private JButton writeButton;

    private JTextArea console;
    private JScrollPane scroll;

    private File workingDirectory = gameData;
    private File recolourFile;
    private File defaultFile;

    // the templates are at the bottom of this file
    private String[] recolourTemplate = getRecolourTemplate();
    private String[] defaultTemplate = getDefaultTemplate();

    public recolourGUI(int width, int height)
    {
        window = new JFrame("gaming");

        workingDirectorySelectButton = new JButton("Select working directory");
        recolourFileSelectButton = new JButton("Select recolour file");
        defaultFileSelectButton = new JButton("Select default file");
        writeButton = new JButton("Write to file");

        console = new JTextArea("console messages:\n");
        console.setEditable(false);
        console.setTabSize(2);

        scroll = new JScrollPane(console);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        this.height = height;
        this.width = width;

        buildUI();
    }

    public static void main(String[] args) {launch(args);}

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

    public void buildUI()
    {
        window.setSize(width, height);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(layout);

        addComponent(workingDirectorySelectButton, 0, 0, 1, 1, GridBagConstraints.NORTHWEST);
        addComponent(recolourFileSelectButton, 1, 1, 1, 1, GridBagConstraints.NORTHWEST);
        addComponent(defaultFileSelectButton, 2, 2, 1, 1, GridBagConstraints.NORTHWEST);
        addComponent(writeButton, 3, 3, 1, 1, GridBagConstraints.NORTHWEST);

        addComponent(scroll, 0, 4, 10, 10, GridBagConstraints.NORTHWEST);

        this.addButtonListeners();
        window.setVisible(true);
    }

    public void addComponent (Component component, int gridx, int gridy, int gridwidth, int gridheight, int anchor)
    {
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = gridx;
        constraints.gridy = gridy;
        constraints.gridwidth = gridwidth;
        constraints.gridheight = gridheight;

        constraints.anchor = anchor;

        layout.setConstraints(component, constraints);
        window.add(component);
    }

    // responses for when buttons are pressed
    private void addButtonListeners()
    {
        ActionListener workingDirectorySelectButtonListener = e ->
        {
            workingDirectory = selectFolder("Select recolor working directory");
            console.append("working directory selected: " + (workingDirectory == null ? "null" : workingDirectory.getPath()) + "\n");
        };
        workingDirectorySelectButton.addActionListener(workingDirectorySelectButtonListener);

        ActionListener recolourFileSelectButtonListener = e ->
        {
            recolourFile = selectConfigFile("Select the file with recolour configs");
            console.append("recolour file selected: " + (recolourFile == null ? "null" : recolourFile.getPath()) + "\n");
        };
        recolourFileSelectButton.addActionListener(recolourFileSelectButtonListener);

        ActionListener defaultFileSelectButtonListener = e ->
        {
            defaultFile = selectConfigFile("Select the file with default configs");
            console.append("default file selected: " + (recolourFile == null ? "null" : defaultFile.getPath()) + "\n");
        };
        defaultFileSelectButton.addActionListener(defaultFileSelectButtonListener);

        ActionListener writeButtonListener = e ->
        {
            writeToFile();
        };
        writeButton.addActionListener(writeButtonListener);
    }

    private void writeToFile()
    {
        try
        {
            if (recolourFile == null || !recolourFile.exists())
            {
                console.append("recolour file has not been selected or does not exist");
                return;
            }
            if (defaultFile == null || !defaultFile.exists())
            {
                console.append("default file has not been selected or does not exist");
                return;
            }

            FileWriter recolourWriter = new FileWriter(recolourFile.getPath());
            FileWriter defaultWriter = new FileWriter(defaultFile.getPath());

            // write to the recolour / default files
            console.append("writing to file: " + recolourFile.getPath() + ":\n");
            for (String s : recolourTemplate)
            {
                console.append(s);
                recolourWriter.write(s);
            }
            recolourWriter.close();

            console.append("\nwriting to file: " + defaultFile.getPath() + ":\n");
            for (String s : defaultTemplate)
            {
                console.append(s);
                defaultWriter.write(s);
            }
            defaultWriter.close();
        }
        catch (Exception e)
        {
            console.append("ah beans: " + e);
        }
    }

    // only lets folders be selected
    private File selectFolder(String message)
    {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setCurrentDirectory(gameData);

        int returnVal = fileChooser.showDialog(window, message);

        if (returnVal == 0) // if a file was selected
        {
            return fileChooser.getSelectedFile();
        }
        else
        {
            return null;
        }
    }

    private File selectConfigFile(String message)
    {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new FileNameExtensionFilter("Config files", "cfg"));
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setCurrentDirectory(workingDirectory);

        int returnVal = fileChooser.showDialog(window, message);

        if (returnVal == 0) // if a file was selected
        {
            return fileChooser.getSelectedFile();
        }
        else
        {
            return null;
        }
    }

    // this is just so its out of the way
    public static String[] getDefaultTemplate()
    {
        String[] output =
                {
                        "// default template //\n",
                        "@PART[]:FOR[]:NEEDS[TexturesUnlimited&]\n",
                        "{\n",
                        "\tMODULE\n",
                        "\t{\n",
                        "\t\tname = KSPTextureSwitch\n",
                        "\t\tsectionName =\n",
                        "\t\tcurrentTextureSet =\n",
                        "\t\ttextureSet =\n",
                        "\t}\n",
                        "}\n",
                        "+KSP_TEXTURE_SET[]:NEEDS[TexturesUnlimited&NearFutureLaunchVehicles]\n",
                        "{\n",
                        "\t@name =\n",
                        "\t@MATERIAL\n",
                        "\t{\n",
                        "\t\ttexture = _MainTex,\n",
                        "\t\ttexture = _BumpMap,\n",
                        "\n",
                        "\t\tPROPERTY\n",
                        "\t\t{\n",
                        "\t\t\tname =\n",
                        "\t\t\tfloat =\n",
                        "\t\t}\n",
                        "\t}\n",
                        "}\n"
                };
        return output;
    }

    public static String[] getRecolourTemplate()
    {
        String[] output =
                {
                        "// recolour template //\n",
                        "@PART[]:FOR[]:NEEDS[TexturesUnlimited&]\n",
                        "{\n",
                        "\t@MODULE[KSPTextureSwitch],0\n",
                        "\t{\n",
                        "\t\ttextureSet =\n",
                        "\t}\n",
                        "\t%MODULE[SSTURecolorGUI]\n",
                        "\t{\n",
                        "\t\t%name = SSTURecolorGUI\n",
                        "\t}\n",
                        "}\n",
                        "+KSP_TEXTURE_SET[]:NEEDS[TexturesUnlimited&]\n",
                        "{\n",
                        "\t@name =\n",
                        "\t@MATERIAL\n",
                        "\t{\n",
                        "\t\tvector = _DiffuseNorm,\n",
                        "\t\tvector = _MetalNorm,\n",
                        "\t\tvector = _SmoothnessNorm,\n",
                        "\n",
                        "\t\ttexture = _MainTex,\n",
                        "\t\ttexture = _BumpMap,\n",
                        "\t\ttexture = _Emissive,\n",
                        "\t\ttexture = _MetallicGlossMap,\n",
                        "\t\ttexture = _MaskTex,\n",
                        "\t}\n",
                        "}\n"
                };
        return output;
    }

}