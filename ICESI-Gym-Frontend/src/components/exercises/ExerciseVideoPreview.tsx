import { Box, Typography } from "@mui/material";

type Props = {
  previewUrl: string | null;
  isDirectVideo: boolean;
  exerciseName: string;
};

const ExerciseVideoPreview = ({
  previewUrl,
  isDirectVideo,
  exerciseName,
}: Props) => {
  return (
    <Box
      sx={{
        width: "100%",
        overflow: "hidden",
        borderRadius: 2,
        bgcolor: "grey.100",
      }}
    >
      {previewUrl ? (
        isDirectVideo ? (
          <Box
            component="video"
            controls
            src={previewUrl}
            sx={{
              width: "100%",
              maxHeight: 420,
              display: "block",
              bgcolor: "black",
            }}
          />
        ) : (
          <Box
            component="iframe"
            src={previewUrl}
            title={`Video de ${exerciseName}`}
            allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
            allowFullScreen
            sx={{
              width: "100%",
              aspectRatio: "16 / 9",
              border: 0,
              display: "block",
            }}
          />
        )
      ) : (
        <Box
          sx={{
            aspectRatio: "16 / 9",
            display: "grid",
            placeItems: "center",
            px: 2,
            textAlign: "center",
            color: "text.secondary",
          }}
        >
          <Typography variant="body2">
            No hay video asociado a este ejercicio.
          </Typography>
        </Box>
      )}
    </Box>
  );
};

export default ExerciseVideoPreview;
