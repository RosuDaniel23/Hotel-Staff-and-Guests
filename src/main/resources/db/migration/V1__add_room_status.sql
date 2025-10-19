-- Add status column to room table if it doesn't exist
ALTER TABLE room ADD COLUMN IF NOT EXISTS status VARCHAR(255);

-- Set default value for existing rows
UPDATE room SET status = 'AVAILABLE' WHERE status IS NULL;

-- Make the column NOT NULL
ALTER TABLE room ALTER COLUMN status SET NOT NULL;
