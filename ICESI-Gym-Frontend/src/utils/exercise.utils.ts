import type { ExerciseRequest, ExerciseResponse } from "../types/api.types";

export const getDifficultyColor = (difficulty?: string) => {
  if (!difficulty) {return "default";}
  const diff = difficulty.toLowerCase();
  if (diff.includes("baja") || diff.includes("beginner")) {return "success";}
  if (diff.includes("media") || diff.includes("intermediate")) {return "warning";}
  if (
    diff.includes("alta") ||
    diff.includes("advanced") ||
    diff.includes("difícil")
  )
    {return "error";}
  return "default";
};

export const getEmbedUrl = (url?: string) => {
  if (!url) {return null;}
  
  let processedUrl = url.trim();
  const isPageSecure = window.location.protocol === 'https:';
  
  if (isPageSecure) {
    // Replace http with https
    if (processedUrl.startsWith('http://')) {
      processedUrl = 'https://' + processedUrl.substring(7);
    } 
    // If it doesn't have any protocol and doesn't look like a relative path, add https
    else if (!processedUrl.match(/^[a-zA-Z]+:\/\//) && !processedUrl.startsWith('/') && !processedUrl.startsWith('.')) {
      processedUrl = 'https://' + processedUrl;
    }
  }

  const youtubeMatch = processedUrl.match(
    /(?:youtu\.be\/|youtube\.com\/(?:embed\/|v\/|watch\?v=|watch\?.+&v=))([\w-]{11})/,
  );
  if (youtubeMatch) {return `https://www.youtube.com/embed/${youtubeMatch[1]}`;}
  
  const vimeoMatch = processedUrl.match(/(?:vimeo\.com\/)(\d+)/);
  if (vimeoMatch) {return `https://player.vimeo.com/video/${vimeoMatch[1]}`;}
  
  return processedUrl;
};

export const getVisibleExercises = (
  exercises: ExerciseResponse[],
  _currentUserId: number | null,
  _isAdmin: boolean,
): ExerciseResponse[] => {
  return exercises;
};

export const buildExercisePayload = ({
  name,
  type,
  description,
  durationMin,
  difficulty,
  videoUrl,
  currentUserId,
  isTrainer,
}: {
  name: string;
  type: string;
  description: string;
  durationMin: string;
  difficulty: string;
  videoUrl: string;
  currentUserId: number | null;
  isTrainer: boolean;
}): ExerciseRequest => {
  return {
    name: name.trim(),
    type: type.trim(),
    description: description.trim() || undefined,
    durationMin: durationMin.trim() ? Number(durationMin) : undefined,
    difficulty: difficulty.trim() || undefined,
    videoUrl: videoUrl.trim() || undefined,
    isPredefined: isTrainer,
    userId: currentUserId ?? undefined,
  };
};
