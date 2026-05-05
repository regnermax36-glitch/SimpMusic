#ifndef MAXREGNER_AUDIO_ENGINE_H
#define MAXREGNER_AUDIO_ENGINE_H

class AudioEngine {
public:
    AudioEngine();
    ~AudioEngine();

    bool start();
    void stop();

private:
    bool isRunning;
};

#endif //MAXREGNER_AUDIO_ENGINE_H