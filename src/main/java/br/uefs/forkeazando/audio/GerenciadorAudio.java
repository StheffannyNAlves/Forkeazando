package br.uefs.forkeazando.audio;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class GerenciadorAudio {


    private static final String CAMINHO = "/audio/8_bit_ice_cave_lofi.wav";

    private Clip clip;
    private FloatControl gainControl;
    private int volume = 100; // guarda o volume mesmo antes do clip existir

    private void carregar() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        URL recurso = GerenciadorAudio.class.getResource(CAMINHO);
        if (recurso == null) {
            throw new IOException("Arquivo de música não encontrado: " + CAMINHO);
        }
        try (AudioInputStream fluxo = AudioSystem.getAudioInputStream(recurso)) {
            clip = AudioSystem.getClip();
            clip.open(fluxo);
        }
        gainControl = clip.isControlSupported(FloatControl.Type.MASTER_GAIN)
                ? (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN)
                : null;
        aplicarVolume(); // aplica o volume que já tinha sido definido
    }

    public void setVolume(int volume) {
        this.volume = Math.max(0, Math.min(100, volume));
        aplicarVolume();
    }

    private void aplicarVolume() {
        if (gainControl == null) return;
        if (volume <= 0) {
            gainControl.setValue(gainControl.getMinimum());
        } else {
            float dB = 20f * (float) Math.log10(volume / 100.0f);
            gainControl.setValue(Math.max(gainControl.getMinimum(),
                    Math.min(gainControl.getMaximum(), dB)));
        }
    }

    public void iniciar() {
        if (clip != null && clip.isRunning()) return; // evita iniciar duas vezes
        try {
            if (clip == null) carregar();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            System.err.println("Áudio indisponível: " + e.getMessage());
        }
    }

    public void parar() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    public void fechar() {
        if (clip != null) {
            clip.stop();
            clip.close();
            clip = null;
            gainControl = null;
        }
    }
}